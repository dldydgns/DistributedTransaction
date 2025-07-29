package com.fisa.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRequestDTO {
    private int userid = 1;
    private int base_account;
    private int quote_account;
    private String type;
    private int amount;

    // 추가 필드
    private String direction;
    private String base;
    private String quote;
    private int base_amount;
    private int quote_amount;
    private double rate;
    private UUID guid;
}
