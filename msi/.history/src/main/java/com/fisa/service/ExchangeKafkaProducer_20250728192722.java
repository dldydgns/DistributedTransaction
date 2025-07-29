package com.fisa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;
import com.fisa.repository.ExchangeRequestRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ExchangeRequestRepository exchangeRequestRepository;


    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, ExchangeRequestRepository exchangeRequestRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }


    public void sendCompTx(String TOPIC, String message) {
        try {
            kafkaTemplate.send(TOPIC, message).whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Kafka 전송 성공");
                } else {
                    kafkaTemplate.send(TOPIC+".DLQ", message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
