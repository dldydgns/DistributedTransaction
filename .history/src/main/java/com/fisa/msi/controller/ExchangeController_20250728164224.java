package com.fisa.msi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.msi.dto.ExchangeRequestDTO;
import com.fisa.msi.service.ExchangeKafkaProducer;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeKafkaProducer producer;

    public ExchangeController(ExchangeKafkaProducer producer) {
        this.producer = producer;
    }
    
    @GetMapping
    public ResponseEntity<String> getExchange() {
        return ResponseEntity.ok("POST요청만 허용합니다.");
    }

    @PostMapping
    public ResponseEntity<String> requestExchange(@RequestBody ExchangeRequestDTO request) throws JsonProcessingException {
        // String json = new ObjectMapper().writeValueAsString(request);
        // producer.sendCompTx("USD_Deposit", json);
        // return ResponseEntity.ok("환전 요청이 Kafka로 전송되었습니다.");

        String targetUrl = "http://192.168.0.123:8080/receive"; // 대상 서버의 IP 및 엔드포인트

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);
            return ResponseEntity.ok("✅ 전송 성공: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 전송 실패: " + e.getMessage());
        }
    }
    }

}