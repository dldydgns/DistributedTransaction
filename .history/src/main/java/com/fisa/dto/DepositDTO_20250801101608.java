package com.fisa.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepositDTO {
    private UUID guid;
    private String userid = "1";
    private String accountid; // 계좌번호
    private int amount; // 금액
    private LocalDateTime date;
    private Integer retrycount = 0;
}
