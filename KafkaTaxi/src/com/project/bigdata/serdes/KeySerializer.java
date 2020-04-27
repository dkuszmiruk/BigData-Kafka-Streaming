package com.project.bigdata.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bigdata.model.Key;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KeySerializer implements Serializer<Key> {
    private boolean isKey;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.isKey = b;
    }

    @Override
    public byte[] serialize(String topic, Key key) {
        if (key == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(key);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing value", e);
        }
    }

    @Override
    public void close() {

    }
}
