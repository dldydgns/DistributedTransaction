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

    private int id;
    private int user_id;
    private String direction;
    private String base;
    private String quote;
    private int base_amount;
    private int quote_amount;
    private double rate;
    private int guid;
            int userid = jsonNode.get("userid").asInt();
            String type = jsonNode.get("type").asText();
            int amount = jsonNode.get("amount").asInt();

            System.out.println("userid" + userid);
            System.out.println("type" + type);
            System.out.println("amount: " + amount);

            kafkaTemplate.send(TOPIC, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
