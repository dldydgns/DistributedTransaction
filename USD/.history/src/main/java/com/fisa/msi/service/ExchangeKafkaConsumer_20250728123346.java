package com.fisa.msi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.msi.repository.CashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ExchangeKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CashRepository cashRepository;

    @Autowired
    public ExchangeKafkaConsumer(CashRepository studentRepository) {
        this.cashRepository = studentRepository;
    }

    @KafkaListener(topics = "USD_Deposit", groupId = "fisa")
    public void receiveExchangeRequest(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String type = jsonNode.get("type").asText();
            int amount = jsonNode.get("amount").asInt();

            // JPA에 저장할 객체 생성
            Cash cash = new Cash();
            cash.setType(type);
            cash.setAmount(amount);

            // 저장
            cashRepository.save(cash);

            System.out.println("💰 저장 완료: " + cash);

        } catch (Exception e) {
            System.err.println("Kafka 메시지 처리 중 오류 발생");
            e.printStackTrace();
        }
    }
}
