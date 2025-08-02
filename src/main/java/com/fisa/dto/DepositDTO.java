package com.fisa.dto;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private String userid = "1";       // 기본값 "1"
    private UUID guid;
    private String accountid;
    private int amount;
    private LocalDateTime date;
    private Integer retryCount;        // 재시도 카운트 (null 가능)
}
