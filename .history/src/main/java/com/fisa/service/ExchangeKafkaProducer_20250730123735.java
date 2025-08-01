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

    public void sendCompTxForWithdraw(String compServerName, WithdrawalDTO withdrawalDTO) {
        try {
            DepositDTO compDeposit = modelMapper.map(withdrawalDTO, DepositDTO.class);
            String compJson = objectMapper.writeValueAsString(compDeposit);
            sendCompTx(compServerName + "Deposit", compJson);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 보상 트랜잭션 전송 실패: " + e.getMessage());
            // TODO: 알림, 로그, DB 적재 등
        }
    }
}
