package com.fisa.service;

import java.sql.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
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
import com.fisa.entity.ExchangeRequest;
import com.fisa.repository.ExchangeRequestRepository;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;
    private final ObjectMapper objectMapper;
    private final ExchangeRequestRepository exchangeRequestRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    
    final String wonhwa = "http://127.0.0.1/";
    final String oehwa = "http://127.0.0.1/";

    final String wonhwaServerName = "Wonhwa";
    final String oehwaServerName = "oehwa";
    

    public ExchangeService(ExchangeKafkaProducer producer, ObjectMapper objectMapper, ExchangeRequestRepository exchangeRequestRepository) {
        this.producer = producer;
        this.objectMapper = objectMapper;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    public ResponseEntity<String> requestExchange(ExchangeRequestDTO requestDTO) {
        try {
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

            if (postJson(withdrawalUrl, withdrawalJson).getStatusCode() == HttpStatus.OK) {

                // 입금 요청 생성 및 전송
                DepositDTO depositDTO = DTOMapper.toDepositDTO(requestDTO, direction);
                String depositJson = objectMapper.writeValueAsString(depositDTO);

                if(postJson(depositUrl, depositJson).getStatusCode() == HttpStatus.OK) {
                    
                    ExchangeRequest requestEntity = mapper.map(requestDTO, ExchangeRequest.class);
                    exchangeRequestRepository.save(requestEntity);

                    return ResponseEntity.status(HttpStatus.OK).body("환전에 성공했습니다.");

                } else {
                    // 출금 취소 보상 트랜잭션 생성 및 전송(출금 취소 = 재입금요청)
                    DepositDTO compTransDTO = mapper.map(withdrawalDTO, DepositDTO.class);

                    String compTransJson = objectMapper.writeValueAsString(compTransDTO);
                    producer.sendCompTx(compServerName + "Deposit", compTransJson);
                    
                    return ResponseEntity.status(512).body("입금에 실패했습니다.");
                }

            } else {
                return ResponseEntity.status(512).body("출금에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환전에 실패했습니다.");
        }
    }

    public ResponseEntity<String> postJson(String url, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
