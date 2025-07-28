package com.fisa.msi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.msi.service.ExchangeKafkaProducer;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeKafkaProducer producer;

    public ExchangeController(ExchangeKafkaProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> requestExchange(@RequestBody String requestJson) {
        producer.sendExchangeRequest(requestJson);
        return ResponseEntity.ok("환전 요청이 Kafka로 전송되었습니다.");
    }

}