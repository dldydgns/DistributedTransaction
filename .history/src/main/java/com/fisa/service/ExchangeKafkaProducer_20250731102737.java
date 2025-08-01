package com.fisa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.dto.DepositDTO;
import com.fisa.dto.WithdrawalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public ExchangeKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper(); // 또는 빈으로 주입 가능
        this.modelMapper = new ModelMapper();   // 또는 빈으로 주입 가능
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
