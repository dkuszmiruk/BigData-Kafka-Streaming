package com.project.bigdata;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.project.bigdata.model.TaxiZoneRecord;
import com.project.bigdata.model.TaxiTripRecord;
import com.project.bigdata.model.anomaly.AnomalyAggregation;
import com.project.bigdata.model.etl.ETLAggregation;
import com.project.bigdata.model.Key;
import com.project.bigdata.serdes.TaxiTripDeserializer;
import com.project.bigdata.serdes.TaxiTripSerializer;
import com.project.bigdata.serdes.anomaly.AnomalyAggregationDeserializer;
import com.project.bigdata.serdes.anomaly.AnomalyAggregationSerializer;
import com.project.bigdata.serdes.etl.*;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class TaxiTripProcessing {

    public static void main(String[] args) {
        Long D = 24L;
        Long L = 4000L;
        String pathToFile ="/home/dominik/Pulpit/data/taxi_zone_lookup.csv";
        if(args.length > 0)
            pathToFile = args[0];
        if(args.length > 2) {
            D = Long.valueOf(args[1]);
            L = Long.valueOf(args[2]);
        }

        Properties config = new Properties();
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "taxi-trip-project");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, EventTimeExtractor.class);

        final Serde<String> stringSerde = Serdes.String();
        final Serde<ETLAggregation> etlAggregationSerde =
                Serdes.serdeFrom(new ETLAggregationSerializer(), new ETLAggregationDeserializer());
        final Serde<TaxiTripRecord> taxiTripRecordSerde  =
                Serdes.serdeFrom(new TaxiTripSerializer(), new TaxiTripDeserializer());
        final Serde<AnomalyAggregation> anomalyAggregationSerde  =
                Serdes.serdeFrom(new AnomalyAggregationSerializer(), new AnomalyAggregationDeserializer());


        final StreamsBuilder builder = new StreamsBuilder();

        HashMap<Long, TaxiZoneRecord> taxiZones = new HashMap<>();
        try {
            fetchFromCSV(pathToFile)
                    .stream().forEach(el -> taxiZones.put(Long.parseLong(el[0]) ,new TaxiZoneRecord(el[1],el[2],el[3])));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        KStream<String, String> textLines = builder.stream("taxi-input", Consumed.with(stringSerde, stringSerde));

        KStream<String,TaxiTripRecord> tripStream = textLines.mapValues(TaxiTripRecord::parseFromCSVLine);

        KStream<Key, TaxiTripRecord> tripWithKey = tripStream.map((k, v) ->
                KeyValue.pair(new Key(taxiZones.get(v.getLocationId()).getBorough(),
                        new SimpleDateFormat("yyyy-MM-dd").format(v.getDate())),v));

        KTable<Windowed<String>, ETLAggregation> taxiTripETL = tripWithKey
                .map((k,v) -> KeyValue.pair(k.toString(),v))
                .groupByKey(Grouped.with(stringSerde, taxiTripRecordSerde))
                .windowedBy(TimeWindows.of(Duration.ofDays(1)).advanceBy(Duration.ofHours(1)))
                .aggregate(
                        () -> new ETLAggregation(0L,0L,0L,0L),
                        (aggKey, newValue,aggValue) -> aggValue.update(newValue),
                        Materialized.<String, ETLAggregation, WindowStore<Bytes, byte[]>>as("etl-store")
                                .withValueSerde(etlAggregationSerde).withKeySerde(stringSerde)
                )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));
        taxiTripETL.toStream().map((k,v)->KeyValue.pair(k.key(),v))
                .to("taxi-etl", Produced.with(stringSerde,etlAggregationSerde));
//                        .foreach((k,v) -> System.out.println(
//                         " " + k.key() + " " + v));




        KTable<Windowed<String>, AnomalyAggregation> taxiAnomalies = tripWithKey
                .groupBy((k,v) -> k.getBorough(),Grouped.with(stringSerde, taxiTripRecordSerde))
                .windowedBy(TimeWindows.of(Duration.ofHours(D)).advanceBy(Duration.ofHours(1)))
                .aggregate(
                        () -> new AnomalyAggregation(0L,0L,0L),
                        (aggKey, newValue,aggValue) -> aggValue.update(newValue),
                        Materialized.<String, AnomalyAggregation, WindowStore<Bytes, byte[]>>as("anomalies-store")
                                .withValueSerde(anomalyAggregationSerde).withKeySerde(stringSerde)
                )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));
        final Long l = L;

        taxiAnomalies.toStream().filter((k,v)-> (v.getDifference() > l))
                .map((v,k)-> KeyValue.pair("Start: " +
                        LocalDateTime.ofInstant(v.window().startTime(),TimeZone.getDefault().toZoneId()) + " stop: "
                                + LocalDateTime.ofInstant(v.window().endTime(),TimeZone.getDefault().toZoneId())
                                + " borough: " + v.key()
                        , " out: " + k.getNumberOfOutgoingPeople() + " to: "
                                + k.getNumberOfIncomingPeople() + " difference: " + k.getDifference()))
                .to("taxi-anomalies");




        final Topology topology = builder.build();
//        System.out.println(topology.describe());
        KafkaStreams streams = new KafkaStreams(topology, config);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
//        KafkaStreams streams = new KafkaStreams(builder.build(), config);
//        streams.start();

    }

    private static List<String[]> fetchFromCSV(String filePath) throws CsvException, IOException {
//        CSVParser csvParser = new CSVParserBuilder()
//                .withSeparator(',')
//                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1)
//                .withCSVParser(csvParser)
                .build();
        return reader.readAll();
    }

}