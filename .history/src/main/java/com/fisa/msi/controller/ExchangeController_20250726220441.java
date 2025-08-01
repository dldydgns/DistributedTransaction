package com.fisa.msi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.msi.dto.ExchangeRequest;
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
    public ResponseEntity<String> requestExchange(@RequestBody ExchangeRequest request) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(request);
        System.out.println("Serialize된 JSON: " + json);
        producer.sendCompTx("USD_Deposit", json);
        return ResponseEntity.ok("환전 요청이 Kafka로 전송되었습니다.");
    }

}