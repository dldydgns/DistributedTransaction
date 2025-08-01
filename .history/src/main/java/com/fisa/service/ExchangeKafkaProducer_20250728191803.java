package com.fisa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;
import com.fisa.repository.ExchangeRequestRepository;

import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
            // send comp
            JsonNode jsonNode = objectMapper.readTree(message);
            
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Kafka 전송 성공");

                    try {

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
    
                        exchangeRequestRepository.save(exchangeRequest);

                    } catch (Exception logEx) {
                        System.err.println("로그 DB 기록 실패: " + logEx.getMessage());
                        kafkaTemplate.send("ExchangeServerLog.DLQ", message);
                    }
                } else {
                    kafkaTemplate.send(TOPIC+".DLQ", message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
