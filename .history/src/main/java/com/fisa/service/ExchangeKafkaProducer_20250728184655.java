package com.fisa.msi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.msi.entity.ExchangeRequest;
import com.fisa.msi.repository.ExchangeRequestRepository;

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
            
            kafkaTemplate.send(TOPIC, message).addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Kafka 전송 성공");
                    exchangeRequestRepository.save(exchangeRequest);
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.err.println("Kafka 전송 실패: " + ex.getMessage());
                    // TODO: 재전송 로직 또는 별도 처리 구현
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
