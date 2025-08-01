package com.fisa.msi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExchangeKafkaProducer {

    private static final String TOPIC = "exchange-request"; // 전송할 Kafka 토픽명
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExchangeRequest(String message) {
        
        try {
            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(message);
            String from = jsonNode.get("from").asText();
            String to = jsonNode.get("to").asText();
            int amount = jsonNode.get("amount").asInt();

            System.out.println("from: " + from);
            System.out.println("to: " + to);
            System.out.println("amount: " + amount);

            // Kafka로 메시지 전송
            kafkaTemplate.send(TOPIC, message);
            System.out.println("Kafka 메시지 발송: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}