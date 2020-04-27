package com.project.bigdata.serdes.etl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bigdata.model.etl.ETLAggregation;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ETLAggregationSerializer implements Serializer<ETLAggregation> {
    private boolean isKey;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.isKey = b;
    }

    @Override
    public byte[] serialize(String topic, ETLAggregation etlAggregation) {
        if (etlAggregation == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(etlAggregation);

        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing value", e);
        }
    }

    @Override
    public void close() {

    }
}
