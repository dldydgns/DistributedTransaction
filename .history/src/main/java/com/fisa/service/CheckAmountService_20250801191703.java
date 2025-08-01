package com.fisa.service;

import com.fisa.dto.CheckAmountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CheckAmountService {

    private final RestTemplate restTemplate = new RestTemplate();

    final String wonhwa = "http://192.168.0.41:8080/checkamount";
    final String oehwa = "http://192.168.0.41:8082/checkamount";

    public CheckAmountDTO OehwaCheck() {
        return requestCheckAmount(oehwa);
    }

    public CheckAmountDTO WonhwaCheck() {
        return requestCheckAmount(wonhwa);
    }

    private CheckAmountDTO requestCheckAmount(String url) {
        try {
            ResponseEntity<CheckAmountDTO> response = restTemplate.getForEntity(url, CheckAmountDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.err.println("❌ 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
