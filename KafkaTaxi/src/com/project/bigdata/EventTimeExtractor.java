package com.project.bigdata;

import com.project.bigdata.model.TaxiTripRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class EventTimeExtractor implements TimestampExtractor {
    public long extract(final ConsumerRecord<Object, Object> record,
                        final long previousTimestamp) {
        long timestamp = -1;
        String stringLine;

        if (record.value() instanceof String) {
            stringLine = (String) record.value();
            timestamp = TaxiTripRecord.parseFromCSVLine(stringLine).getTimestampInMillis();
        }
        return timestamp;
    }
}