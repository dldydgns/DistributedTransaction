package com.fisa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fisa.dto.ExchangeRequestDTO;
import com.fisa.service.ExchangeKafkaProducer;
import com.fisa.service.ExchangeService;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final ExchangeKafkaProducer producer;

    public ExchangeController(ExchangeService exchangeService, ExchangeKafkaProducer producer) {
        this.exchangeService = exchangeService;
        this.producer = producer;
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
        // 출금 취소 보상 트랜잭션 생성 및 전송(출금 취소 = 재입금요청)
        DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);

        String compTransJson = objectMapper.writeValueAsString(compTransDTO);
        producer.sendCompTx(compServerName + "Deposit", compTransJson);
        return exchangeService.requestExchange(request);
    }
}