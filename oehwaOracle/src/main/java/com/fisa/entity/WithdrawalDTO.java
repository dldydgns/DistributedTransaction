package com.fisa.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WithdrawalDTO {
    private int userid = 1;
    private int accountid;
    private int amount;
    private Date date;
}