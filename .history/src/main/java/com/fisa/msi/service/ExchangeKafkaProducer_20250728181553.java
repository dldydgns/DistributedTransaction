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

            int id = jsonNode.get("id").asInt();
            int userid = jsonNode.get("userid").asInt();
            String direction = jsonNode.get("direction").asText();
            String base;
            String quote;
            int base_amount;
            int quote_amount;
            double rate;
            int guid;

            System.out.println("userid" + userid);
            System.out.println("type" + type);
            System.out.println("amount: " + amount);

            kafkaTemplate.send(TOPIC, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
