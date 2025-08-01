package com.fisa.entity;

public class ExchangeStatus {
    public ExchangeStatus(String code, String description) {
        @Id
        private String code;      // 상태 코드 예: PENDING, COMPLETE, FAILED
    
        private String description;  // 상태 설명
    }
}
