package com.fisa.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepositDTO {
    private int userid = 1;
    private int accountid;
    private int amount;
    private LocalDateTime date;
}
