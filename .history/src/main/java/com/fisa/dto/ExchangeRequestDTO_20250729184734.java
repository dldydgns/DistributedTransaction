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
public class ExchangeRequestDTO {
    private int userid = 1;
    private String direction;

    private String base;
    private int base_account;
    private int base_amount;

    private String quote;
    private int quote_account;
    private int quote_amount;
    
    private double rate;
    private UUID guid;

    private String status;
    private LocalDateTime date;
}
