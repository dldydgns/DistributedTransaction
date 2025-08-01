package com.fisa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeStatus {

    @Id
    private String code;      // 상태 코드 예: PENDING, COMPLETE, FAILED

    private String description;  // 상태 설명

    // 추가 생성자 (선택)
    public ExchangeStatus(String code) {
        this.code = code;
    }
}
