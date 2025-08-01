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
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String type = jsonNode.get("deposit").asText();
            int amount = jsonNode.get("amount").asInt();

            System.out.println("deposit" + type);
            System.out.println("amount: " + amount);

            kafkaTemplate.send(TOPIC, message);
            System.out.println("Kafka 메시지 발송: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
