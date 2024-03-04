package com.example.rqchallenge.employees.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMapper {

    private static final ThreadLocal<ObjectMapper> om = ThreadLocal.withInitial(() -> {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    });

    public static ObjectMapper getObjectMapper() {
        return om.get();
    }
}
