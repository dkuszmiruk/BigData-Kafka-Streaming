package com.project.bigdata.serdes.anomaly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bigdata.model.anomaly.AnomalyAggregation;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class AnomalyAggregationDeserializer implements Deserializer<AnomalyAggregation> {
    private boolean isKey;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.isKey = b;
    }

    @Override
    public AnomalyAggregation deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        try {
            return objectMapper.readValue(new String(bytes,"UTF-8"), AnomalyAggregation.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing value", e);
        }
    }

    @Override
    public void close() {

    }
}
