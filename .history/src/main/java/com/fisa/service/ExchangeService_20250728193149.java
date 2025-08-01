package com.fisa.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;

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

            ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .user_id(jsonNode.get("userid").asInt())
                .direction(jsonNode.get("direction").asText())
                .base(jsonNode.get("base").asText())
                .quote(jsonNode.get("quote").asText())
                .base_amount(jsonNode.get("base_amount").asInt())
                .quote_amount(jsonNode.get("quote_amount").asInt())
                .rate(jsonNode.get("rate").asDouble())
                .guid(UUID.fromString(jsonNode.get("guid").asText()))
                .build();


        } catch(Exception e) {

        }
        producer.sendCompTx(TOPIC, requestJson);
    }
}
