package com.project.bigdata.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bigdata.model.TaxiTripRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class TaxiTripDeserializer implements Deserializer<TaxiTripRecord> {
    private boolean isKey;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        this.isKey = b;
    }

    @Override
    public TaxiTripRecord deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        try {
            return objectMapper.readValue(new String(bytes,"UTF-8"), TaxiTripRecord.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing value", e);
        }
    }

    @Override
    public void close() {

    }
}
