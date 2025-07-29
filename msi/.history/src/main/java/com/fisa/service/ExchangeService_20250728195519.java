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

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    
    String wonhwa = "http://127.0.0.1/";
    String oehwa = "http://127.0.0.1/";

    public ExchangeService(ExchangeKafkaProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public void requestExchange(String TOPIC, String requestJson) {

        try {
            String url = null;
        
            JsonNode jsonNode = objectMapper.readTree(requestJson);

            String direction = jsonNode.get("direction").asText();
            String base = jsonNode.get("base").asText();
            String quote = jsonNode.get("quote").asText();
            int base_amount = jsonNode.get("base_amount").asInt();
            int quote_amount = jsonNode.get("quote_amount").asInt();

            url = base=="KRW" ? wonhwa : oehwa;

            ResponseEntity<String> response = postJson(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Base 서버 출금 성공");
            } else {
                producer.sendCompTx(TOPIC, requestJson);
            }
        } catch (Exception e) {
            // 바로 출금요청 실패 응답
            
        }
    }

    public ResponseEntity<String> postJson(String url, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
