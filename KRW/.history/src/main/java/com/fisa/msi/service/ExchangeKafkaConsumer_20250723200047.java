package com.fisa.msi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "exchange-request", groupId = "exchange-group")
    public void receiveExchangeRequest(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String from = jsonNode.get("from").asText();
            String to = jsonNode.get("to").asText();
            int amount = jsonNode.get("amount").asInt();

            System.out.println("Kafka 메시지 수신");
            System.out.println("from: " + from);
            System.out.println("to: " + to);
            System.out.println("amount: " + amount);

        } catch (Exception e) {
            System.err.println("Kafka 메시지 처리 중 오류 발생");
            e.printStackTrace();
        }
    }
}
