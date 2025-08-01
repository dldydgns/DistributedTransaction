package com.fisa.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;
    private final ObjectMapper objectMapper;

    public ExchangeService(ExchangeKafkaProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public void requestExchange(String TOPIC, String requestJson) {
        try {
            // send comp
            JsonNode jsonNode = objectMapper.readTree(requestJson);

            String base = jsonNode.get("base").asText();
            String quote = jsonNode.get("quote").asText();
            String direction = jsonNode.get("direction").asText();

        } catch(Exception e) {

        }
        producer.sendCompTx(TOPIC, requestJson);
    }
}
