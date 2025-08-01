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
        String str = "송하 넌 뭐랄까.. 마치 베를린 같아. 왜냐하면 치명적인 독일 수도.";
        kafkaTemplate.send("Server" + topic, str).whenComplete((result, ex) -> {
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
