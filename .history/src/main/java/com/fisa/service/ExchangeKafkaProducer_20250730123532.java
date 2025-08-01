package com.fisa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fisa.dto.WithdrawalDTO;

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


    public void sendCompTxForWithdraw(String compServerName, WithdrawalDTO withdrawalDTO) {
        try {
            // 출금 정보를 입금 DTO로 변환 (보상 트랜잭션: 출금 취소 = 재입금)
            DepositDTO compDeposit = modelMapper.map(withdrawalDTO, DepositDTO.class);
    
            // JSON 직렬화
            String compJson = objectMapper.writeValueAsString(compDeposit);
    
            // Kafka로 보상 트랜잭션 전송
            producer.sendCompTx(serverName + "Deposit", compJson);
    
            System.out.println("🔁 보상 트랜잭션 전송 완료: " + compServerName + "Deposit");
    
        } catch (Exception e) {
            // 보상 트랜잭션 생성 또는 전송 실패 시 로그
            e.printStackTrace();
            System.err.println("❌ 보상 트랜잭션 전송 실패: " + e.getMessage());
            // TODO: 알림, 로그, 파일 기록 등 추가 대응
        }
    }
    
}
