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
    private String userid = "1";
    private String direction;

    private String base;
    private String base_account;
    private int base_amount;

    private String quote;
    private String quote_account;
    private int quote_amount;
    
    private double rate;
    private UUID guid;

    private String status;
    private LocalDateTime date;
}
