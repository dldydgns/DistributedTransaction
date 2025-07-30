package com.fisa.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DepositDTO {
    private String userid = "1";
    private String accountid; // 계좌번호
    private int amount; // 금액
    private LocalDateTime date;

}