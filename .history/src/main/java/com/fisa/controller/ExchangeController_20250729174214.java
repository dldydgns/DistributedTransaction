package com.fisa.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.dto.DepositDTO;
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
        String reqJson = objectMapper.writeValueAsString(requestDTO);
        JsonNode jsonNode = objectMapper.readTree(reqJson);

        // direction이 BUY면 base먼저 출금 후 quote 입금
        // direction이 SELL이면 quote먼저 출금 후 base 입금
        String direction = jsonNode.get("direction").asText();

        String withdrawalUrl = null;
        String depositUrl = null;
        String compServerName = null;

        if(direction.equals("BUY")) {
            withdrawalUrl = (jsonNode.get("base").asText().equals("KRW") ? wonhwa : oehwa);
            depositUrl = (jsonNode.get("quote").asText().equals("KRW") ? wonhwa : oehwa);

            compServerName = jsonNode.get("base").asText().equals("KRW") ? wonhwaServerName : oehwaServerName;
        } else {
            withdrawalUrl = (jsonNode.get("quote").asText().equals("KRW") ? wonhwa : oehwa);
            depositUrl = (jsonNode.get("base").asText().equals("KRW") ? wonhwa : oehwa);

            compServerName = jsonNode.get("quote").asText().equals("KRW") ? wonhwaServerName : oehwaServerName;
        }

        // 출금 요청 생성 및 전송
        WithdrawalDTO withdrawalDTO = DTOMapper.toWithdrawalDTO(requestDTO, direction);
        String withdrawalJson = objectMapper.writeValueAsString(withdrawalDTO);
        // 출금 취소 보상 트랜잭션 생성 및 전송(출금 취소 = 재입금요청)
        ModelMapper modelMapper = new ModelMapper();
        DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String compTransJson = objectMapper.writeValueAsString(compTransDTO);
        producer.sendCompTx(compServerName + "Deposit", compTransJson);
        return exchangeService.requestExchange(request);
    }
}