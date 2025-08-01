package com.fisa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCompTx(String topic, Object dto) {
        kafkaTemplate.send(topic, dto).whenComplete((result, ex) -> {
            if (ex != null) {
                sendDlq(topic, dto);
            }
        });
    }

    public void sendDlq(String dlqTopic, Object dto) {
        kafkaTemplate.send(dlqTopic + ".DLQ", dto).whenComplete((result, ex) -> {
            if (ex != null) {
                // DLQ 전송 실패 처리 (예: 로그 기록, 알림 전송 등)
            }
        });
    }
}
