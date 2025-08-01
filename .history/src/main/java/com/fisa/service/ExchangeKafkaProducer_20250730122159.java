package com.fisa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendCompTx(String topic, String message) {
        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
            if (ex != null) {
                sendDlq(topic, message);
            }
        });
    }
    
    public void sendDlq(String dlqTopic, String message) {
        kafkaTemplate.send(dlqTopic + ".DLQ", message).whenComplete((result, ex) -> {
            if (ex != null) {
                // DLQ 전송 실패 처리 (재시도, 로그, 알림 등)
            }
        });
    }
    
}
