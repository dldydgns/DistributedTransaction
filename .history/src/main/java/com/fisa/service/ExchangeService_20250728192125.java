package com.fisa.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;

    public ExchangeService(ExchangeKafkaProducer producer) {
        this.producer = producer;
    }

    public void requestExchange(String TOPIC, String requestJson) {
        try {
            // send comp
            JsonNode jsonNode = objectMapper.readTree(message);
        } catch(Exception e) {

        }
        producer.sendCompTx(TOPIC, requestJson);
    }
}
