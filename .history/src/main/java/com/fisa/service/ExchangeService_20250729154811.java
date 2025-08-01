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
import com.fisa.dto.DepositDTO;
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

            // direction이 BUY면 base먼저 출금 후 quote 입금
            // direction이 SELL이면 quote먼저 출금 후 base 입금
            String direction = jsonNode.get("direction").asText();
            if(direction.equals("BUY")) {
                withdrawalUrl = (jsonNode.get("base").asText().equals("KRW") ? wonhwa : oehwa);
                depositUrl = (jsonNode.get("quote").asText().equals("KRW") ? wonhwa : oehwa);
            } else {
                depositUrl = (jsonNode.get("base").asText().equals("KRW") ? wonhwa : oehwa);
                withdrawalUrl = (jsonNode.get("quote").asText().equals("KRW") ? wonhwa : oehwa);
            }

            // 출금 요청 생성 및 전송
            WithdrawalDTO withdrawalDTO = DTOMapper.toWithdrawalDTO(request, direction);
            String withdrawalJson = objectMapper.writeValueAsString(withdrawalDTO);
            ResponseEntity<String> response = postJson(direction.equals("BUY") ? baseUrl : quoteUrl, withdrawalJson);

            if (response.getStatusCode() == HttpStatus.OK) {

                // 입금 요청 생성 및 전송
                DepositDTO depositDTO = DTOMapper.toDepositDTO(request, direction);
                String depositJson = objectMapper.writeValueAsString(depositDTO);
                response = postJson(quoteUrl, depositJson);

                if(response.getStatusCode() == HttpStatus.OK) {
                    // todo: DB에 로그 저장

                } else {
                    // 출금 취소 보상 트랜잭션 생성 및 전송
                    DepositDTO compTransDTO = new DepositDTO(withdrawalDTO);
                    String compTransJson = objectMapper.writeValueAsString(compTransDTO);

                    producer.sendCompTx("OehwaWithdrawal", compTransJson);
                    
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("환전에 실패했습니다.");
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잠시 후 재시도 해주세요");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잠시 후 재시도 해주세요");
        }
    }

    public ResponseEntity<String> postJson(String url, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
