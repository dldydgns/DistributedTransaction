package com.fisa.dto;

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
    private int base_account = 1;
    private int quote_account = 1;
    private String type;
    private int amount;
}
