package com.fisa.msi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private static final String TOPIC = "exchange-request"; // 전송할 Kafka 토픽명

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExchangeRequest(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Kafka 메시지 발송: " + message);
    }
}