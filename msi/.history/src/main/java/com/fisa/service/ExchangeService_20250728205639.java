package com.fisa.service;

import java.sql.Date;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fisa.dto.DTOMapper;
import com.fisa.dto.ExchangeRequestDTO;
import com.fisa.dto.WithdrawalDTO;

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

    public ResponseEntity<String> requestExchange(ExchangeRequestDTO request) {

        java.sql.Timestamp date = new java.sql.Timestamp(System.currentTimeMillis());

        try {
            String reqJson = objectMapper.writeValueAsString(request);
            JsonNode jsonNode = objectMapper.readTree(reqJson);

            String direction = jsonNode.get("direction").asText();

            if(direction.equals("BUY")) {

            }

            String baseUrl = (jsonNode.get("base").asText().equals("KRW") ? wonhwa : oehwa);
            String quoteUrl = (jsonNode.get("quote").asText()=="KRW" ? wonhwa : oehwa);

            // 출금 요청 생성 및 전송
            WithdrawalDTO withdrawalDTO = DTOMapper.toWithdrawalDTO(request, true);
            String withdrawalJson = objectMapper.writeValueAsString(withdrawalDTO);
            ResponseEntity<String> response = postJson(baseUrl, withdrawalJson);

            if (response.getStatusCode() == HttpStatus.OK) {
                // todo: 입금요청 DTO로 json생성
                response = postJson(quoteUrl, json);

                if(response.getStatusCode() == HttpStatus.OK) {

                } else {
                    producer.sendCompTx("OehwaWithdrawal", requestJson);
                    
                    return false;
                }

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
