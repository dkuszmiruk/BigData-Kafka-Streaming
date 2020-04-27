package com.project.bigdata.serdes.anomaly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bigdata.model.anomaly.AnomalyAggregation;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class AnomalyAggregationSerializer implements Serializer<AnomalyAggregation> {
    private boolean isKey;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.isKey = b;
    }

    @Override
    public byte[] serialize(String topic, AnomalyAggregation anomalyAggregation) {
        if (anomalyAggregation == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(anomalyAggregation);

        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing value", e);
        }
    }

    @Override
    public void close() {

    }
}
