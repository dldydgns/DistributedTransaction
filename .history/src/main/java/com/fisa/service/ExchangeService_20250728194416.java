package com.fisa.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.entity.ExchangeRequest;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    
    String baseServerUrl = "http://127.0.0.1/api/withdraw";

    public ExchangeService(ExchangeKafkaProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public void requestExchange(String TOPIC, String requestJson) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseServerUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Base 서버 출금 성공");
            } else {
                producer.sendCompTx(TOPIC, requestJson);
            }
        } catch (Exception e) {
            // 바로 출금요청 실패 응답
            
        }
    }
}
