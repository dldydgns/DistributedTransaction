package com.fisa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserBank {

    @Id
    private String id;        // 계좌 고유 ID

    private int account;      // 계좌 잔액

    private String userId;    // 사용자 ID

    private String base;      // 통화 종류 (예: KRW, USD 등)
}
