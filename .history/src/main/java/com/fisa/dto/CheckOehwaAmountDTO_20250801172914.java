package com.fisa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckOehwaAmountDTO {
    private String userid;

    private String quote;
    private String quote_account;
    private int quote_amount;
}
