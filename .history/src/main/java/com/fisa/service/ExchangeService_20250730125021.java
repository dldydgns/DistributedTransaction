package com.fisa.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
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
        try {
            String direction = requestDTO.getDirection();
            String base = requestDTO.getBase();
            String quote = requestDTO.getQuote();
    
            String withdrawalUrl, depositUrl, compServerName;
    
            if (direction.equals("BUY")) {
                withdrawalUrl = "KRW".equals(base) ? wonhwa : oehwa;
                depositUrl = "KRW".equals(quote) ? wonhwa : oehwa;
                compServerName = "KRW".equals(base) ? wonhwaServerName : oehwaServerName;
            } else {
                withdrawalUrl = "KRW".equals(quote) ? wonhwa : oehwa;
                depositUrl = "KRW".equals(base) ? wonhwa : oehwa;
                compServerName = "KRW".equals(quote) ? wonhwaServerName : oehwaServerName;
            }
    
            // 출금 요청
            WithdrawalDTO withdrawalDTO = DTOMapper.toWithdrawalDTO(requestDTO, direction);
            String withdrawalJson = objectMapper.writeValueAsString(withdrawalDTO);

            ResponseEntity<String> withdrawalResponse = postJson(withdrawalUrl + "/withdrawal", withdrawalJson);
            
            if (!withdrawalResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(500).body("출금에 실패했습니다.");
            }
    
            if (postJson(withdrawalUrl+"/withdrawal", withdrawalJson).getStatusCode() == HttpStatus.OK) {

                // 입금 요청 생성 및 전송
                DepositDTO depositDTO = DTOMapper.toDepositDTO(requestDTO, direction);
                String depositJson = objectMapper.writeValueAsString(depositDTO);

                if(postJson(depositUrl+"/deposit", depositJson).getStatusCode() == HttpStatus.OK) {
                    
                    ExchangeRequest requestEntity = modelMapper.map(requestDTO, ExchangeRequest.class);
                    exchangeRequestRepository.save(requestEntity);

                    return ResponseEntity.status(HttpStatus.OK).body("환전에 성공했습니다.");

                } else {
                    // 출금 취소 보상 트랜잭션 생성 및 전송(출금 취소 = 재입금요청)
                    DepositDTO compTransDTO = modelMapper.map(withdrawalDTO, DepositDTO.class);

                    String compTransJson = objectMapper.writeValueAsString(compTransDTO);
                    producer.sendCompTx(compServerName + "Deposit", compTransJson);
                    
                    return ResponseEntity.status(500).body("입금에 실패했습니다.");
                }

            } else {
                return ResponseEntity.status(500).body("출금에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
