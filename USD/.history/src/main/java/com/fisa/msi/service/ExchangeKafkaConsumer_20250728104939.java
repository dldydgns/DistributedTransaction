package com.fisa.msi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.msi.entity.student;
import com.fisa.msi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeKafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StudentRepository studentRepository;

    @Autowired
    public ExchangeKafkaConsumer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @KafkaListener(topics = "USD_Deposit", groupId = "fisa")
    public void receiveExchangeRequest(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String type = jsonNode.get("type").asText();
            int amount = jsonNode.get("amount").asInt();

            System.out.println("type: " + type);
            System.out.println("amount: " + amount);

            

        } catch (Exception e) {
            System.err.println("Kafka 메시지 처리 중 오류 발생");
            e.printStackTrace();
        }
    }
}
