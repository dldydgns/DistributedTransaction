package com.fisa.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.dto.DTOMapper;
import com.fisa.dto.DepositDTO;
import com.fisa.dto.ExchangeRequestDTO;
import com.fisa.dto.WithdrawalDTO;
import com.fisa.entity.ExchangeRequest;
import com.fisa.repository.ExchangeRequestRepository;

@Service
public class ExchangeService {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ExchangeKafkaProducer producer;
    private final ExchangeRequestRepository exchangeRequestRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    
    final String wonhwa = "http://192.168.0.62:8080/api/wonhwa";
    final String oehwa = "http://192.168.0.58:8080/api/oehwa";

    final String wonhwaServerName = "Wonhwa";
    final String oehwaServerName = "oehwa";
    

    public ExchangeService(ObjectMapper objectMapper, ModelMapper modelMapper, ExchangeKafkaProducer producer, ExchangeRequestRepository exchangeRequestRepository) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.producer = producer;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    public ResponseEntity<String> requestExchange(ExchangeRequestDTO requestDTO) {
        ExchangeRequest requestEntity = null;
    
        // 환전 기록 DB 생성
        try {
            requestEntity = modelMapper.map(requestDTO, ExchangeRequest.class);
            requestEntity.setStatus("PENDING");
            exchangeRequestRepository.save(requestEntity);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("환전요청 DB 저장 실패: " + e.getClass().getSimpleName());
        }
    
        // 데이터 전처리
        String direction = requestDTO.getDirection();
        String withdrawalUrl, depositUrl, compServerName;
    
        if (direction.equals("BUY")) {
            withdrawalUrl = "KRW".equals(requestDTO.getBase()) ? wonhwa : oehwa;
            depositUrl = "KRW".equals(requestDTO.getQuote()) ? wonhwa : oehwa;
            compServerName = "KRW".equals(requestDTO.getBase()) ? wonhwaServerName : oehwaServerName;
        } else {
            withdrawalUrl = "KRW".equals(requestDTO.getQuote()) ? wonhwa : oehwa;
            depositUrl = "KRW".equals(requestDTO.getBase()) ? wonhwa : oehwa;
            compServerName = "KRW".equals(requestDTO.getQuote()) ? wonhwaServerName : oehwaServerName;
        }
    
        // 출금 요청
        WithdrawalDTO withdrawalDTO = DTOMapper.toWithdrawalDTO(requestDTO, direction);
        try {
            String withdrawalJson = objectMapper.writeValueAsString(withdrawalDTO);
            ResponseEntity<String> withdrawalResponse = postJson(withdrawalUrl + "/withdrawal", withdrawalJson);
    
            if (!withdrawalResponse.getStatusCode().is2xxSuccessful()) {
                requestEntity.setStatus("FAILED");
                exchangeRequestRepository.save(requestEntity);
                return ResponseEntity.status(500).body("출금에 실패했습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            requestEntity.setStatus("FAILED");
            exchangeRequestRepository.save(requestEntity);
            return ResponseEntity.status(500).body("출금 요청 중 오류 발생: " + e.getClass().getSimpleName());
        }
    
        // 입금 요청
        try {
            DepositDTO depositDTO = DTOMapper.toDepositDTO(requestDTO, direction);
            String depositJson = objectMapper.writeValueAsString(depositDTO);
            ResponseEntity<String> depositResponse = postJson(depositUrl + "/deposit", depositJson);
    
            if (depositResponse.getStatusCode().is2xxSuccessful()) {
                requestEntity.setStatus("COMPLETE");
                exchangeRequestRepository.save(requestEntity);
                return ResponseEntity.status(200).body("환전에 성공했습니다.");
            } else {
                requestEntity.setStatus("FAILED");
                exchangeRequestRepository.save(requestEntity);
                return ResponseEntity.status(500).body("입금에 실패했습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
    
            try {
                // 출금 보상 트랜잭션 실행
                DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);
                String compTransJson = objectMapper.writeValueAsString(compTransDTO);
                producer.sendCompTx(compServerName + "Deposit", compTransJson);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
    
            requestEntity.setStatus("FAILED");
            exchangeRequestRepository.save(requestEntity);
            return ResponseEntity.status(500).body("입금 요청 중 예외 발생: " + e.getClass().getSimpleName());
        }
    }
    

    public ResponseEntity<String> postJson(String url, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
