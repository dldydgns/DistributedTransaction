package com.fisa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.dto.ExchangeRequestDTO;
import com.fisa.service.ExchangeKafkaProducer;
import com.fisa.service.ExchangeService;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }
    
    @GetMapping
    public ResponseEntity<String> getExchange() {
        return ResponseEntity.ok("POST요청만 허용합니다.");
    }

    @PostMapping("usd")
    public ResponseEntity<String> postUSDExchange(@RequestBody ExchangeRequestDTO request) throws JsonProcessingException {
        return exchangeService.requestExchange(request);
    }

    @PostMapping("eu")
    public ResponseEntity<String> postEUExchange(@RequestBody ExchangeRequestDTO request) throws JsonProcessingException {
        return ResponseEntity.ok("환전 요청이 완료되었습니다.");
    }
}