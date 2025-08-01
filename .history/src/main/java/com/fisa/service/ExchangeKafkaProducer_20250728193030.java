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


    public void sendCompTx(String topic, String message) {
        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
            if (ex == null) {
                // 성공 처리 (DB 저장 등)
            } else {
                sendToDlq(topic + ".DLQ", message);
            }
        });
    }
    
    public void sendDql(String dlqTopic, String message) {
        // 재시도, 로깅 등 별도 로직 구현 가능
        kafkaTemplate.send(dlqTopic, message).whenComplete((result, ex) -> {
            if (ex != null) {
                // DLQ 전송 실패 처리 (재시도, 로그, 알림 등)
            }
        });
    }
    
}
