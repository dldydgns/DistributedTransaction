package com.fisa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;
import com.fisa.repository.ExchangeRequestRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
            
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

            // ListenableFuture → CompletableFuture 변환
            CompletableFuture<SendResult<String, String>> completableFuture = future.completable();
            
            completableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Kafka 전송 성공");
                    exchangeRequestRepository.save(exchangeRequest);
                } else {
                    System.err.println("Kafka 전송 실패: " + ex.getMessage());
                    // TODO: 재전송 또는 실패 로그 저장
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
