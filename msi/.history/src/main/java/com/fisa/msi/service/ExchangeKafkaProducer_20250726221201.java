package com.fisa.msi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendCompTx(String TOPIC, String message) {

        System.err.println("Serialize된 JSON: " + message);

        System.err.println("Serialize된 JSON: " + message);

        System.err.println("Serialize된 JSON: " + message);

        System.err.println("Serialize된 JSON: " + message);

        System.err.println("Serialize된 JSON: " + message);

        System.err.println("Serialize된 JSON: " + message);
    }
}
