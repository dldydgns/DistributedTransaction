package com.fisa.service;

import java.util.UUID;

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
import com.fisa.entity.ExchangeStatus;
import com.fisa.repository.ExchangeRequestRepository;
import com.fisa.repository.ExchangeStatusRepository;

@Service
public class ExchangeService {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ExchangeKafkaProducer producer;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ExchangeStatusRepository exchangeStatusRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    
    final String wonhwa = "http://192.168.0.62:8080/api/wonhwa";
    final String oehwa = "http://192.168.0.58:8080/api/oehwa";

    final String wonhwaServerName = "Wonhwa";
    final String oehwaServerName = "oehwa";
    

    public ExchangeService(ObjectMapper objectMapper, ModelMapper modelMapper, ExchangeKafkaProducer producer,
     ExchangeRequestRepository exchangeRequestRepository, ExchangeStatusRepository exchangeStatusRepository) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.producer = producer;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.exchangeStatusRepository = exchangeStatusRepository;
    }

    public ResponseEntity<String> requestExchange(ExchangeRequestDTO requestDTO) {
        ExchangeRequest requestEntity = null;
    
        // 환전 기록 DB 생성
        try {
            requestEntity = modelMapper.map(requestDTO, ExchangeRequest.class);
            exchangeRequestRepository.save(requestEntity);
            exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "시작"));
        } catch(Exception e) {
            e.printStackTrace();
            exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "DB생성 실패"));
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
    
            if (withdrawalResponse.getStatusCode().is2xxSuccessful()) {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 성공"));
            } else {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 실패"));
                return ResponseEntity.status(500).body("출금에 실패했습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 결과 모름"));

            ResponseEntity<String> checkResponse = checkHistoryByGuid(withdrawalUrl + "/check/withdrawal", requestDTO.getGuid());
        
            if(checkResponse.getStatusCode().is2xxSuccessful()) {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 성공 확인"));
                DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);
                String compTransJson = objectMapper.writeValueAsString(compTransDTO);
                producer.sendCompTx(withdrawalUrl, compTransJson);
            } else {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 실패 확인"));
            }

            return ResponseEntity.status(500).body("출금 요청 중 오류 발생: " + e.getClass().getSimpleName());
        }
    
        // 입금 요청
        try {
            DepositDTO depositDTO = DTOMapper.toDepositDTO(requestDTO, direction);
            String depositJson = objectMapper.writeValueAsString(depositDTO);
            ResponseEntity<String> depositResponse = postJson(depositUrl + "/deposit", depositJson);
    
            if (depositResponse.getStatusCode().is2xxSuccessful()) {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "입금 성공"));

                return ResponseEntity.status(200).body("환전에 성공했습니다.");
            } else {
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "입금 실패"));

                DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);
                String compTransJson = objectMapper.writeValueAsString(compTransDTO);
                producer.sendCompTx(compServerName + "Deposit", compTransJson);
                exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "출금 취소"));

                return ResponseEntity.status(500).body("입금에 실패했습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            exchangeStatusRepository.save(new ExchangeStatus(requestEntity, "입금 결과 모름"));
            
            // todo: 입금결과 재확인 결과에 따라 보상트랜잭션 발행

            return ResponseEntity.status(500).body("입금 요청 중 예외 발생: " + e.getClass().getSimpleName());
        }
    }
    
    public ResponseEntity<String> checkHistoryByGuid(String url, UUID guid) {
        String requestUrl = String.format("%s?guid=%s", url, guid.toString());
        return restTemplate.getForEntity(requestUrl, String.class);
    }
    

    public ResponseEntity<String> postJson(String url, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
