package com.fisa.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;
    private final ObjectMapper objectMapper;

    public ExchangeService(ExchangeKafkaProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public void requestExchange(String TOPIC, String requestJson) {
        try {
            // send comp
            JsonNode jsonNode = objectMapper.readTree(requestJson);

            String base = jsonNode.get("base").asText();
            String quote = jsonNode.get("quote").asText();
            String direction = jsonNode.get("direction").asText();

            ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .user_id(jsonNode.get("userid").asInt())
                .direction(jsonNode.get("direction").asText())
                .base(jsonNode.get("base").asText())
                .quote(jsonNode.get("quote").asText())
                .base_amount(jsonNode.get("base_amount").asInt())
                .quote_amount(jsonNode.get("quote_amount").asInt())
                .rate(jsonNode.get("rate").asDouble())
                .guid(UUID.fromString(jsonNode.get("guid").asText()))
                .build();


        } catch(Exception e) {

        }
        producer.sendCompTx(TOPIC, requestJson);
    }

    public void requestExchange(String TOPIC, String requestJson) {
        String baseServerUrl = "http://127.0.0.1/api/withdraw";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseServerUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 200 OK일 때는 sendCompTx 실행하지 않음
                System.out.println("Base 서버 출금 성공, sendCompTx 실행 대기");
            } else {
                // 200 OK 아니면 바로 sendCompTx 실행
                producer.sendCompTx(TOPIC, requestJson);
            }
        } catch (Exception e) {
            // 에러 발생 시 sendCompTx 실행
            producer.sendCompTx(TOPIC, requestJson);
        }
    }
}
