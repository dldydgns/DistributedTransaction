package com.fisa.dto;

import java.sql.Date;
import java.time.Instant;

public class DTOMapper {

    public static WithdrawalDTO toWithdrawalDTO(ExchangeRequestDTO exchangeRequestDTO, String direction) {
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

        withdrawalDTO.setUserid(exchangeRequestDTO.getUserid());
        withdrawalDTO.setAmount(direction.equals("BUY") ? exchangeRequestDTO.getBase_amount() : exchangeRequestDTO.getQuote_amount());
        withdrawalDTO.setAccountid(direction.equals("BUY") ? exchangeRequestDTO.getBase_account() : exchangeRequestDTO.getQuote_account());
        withdrawalDTO.setDate(new Date(Instant.now().toEpochMilli()));

        return withdrawalDTO;
    }

    public static DepositDTO toDepositDTO(ExchangeRequestDTO exchangeRequestDTO, String direction) {
        DepositDTO depositDTO = new DepositDTO();

        depositDTO.setUserid(exchangeRequestDTO.getUserid());
        depositDTO.setAmount(direction.equals("BUY") ? exchangeRequestDTO.getQuote_amount() : exchangeRequestDTO.getBase_amount());
        depositDTO.setAccountid(direction.equals("BUY") ? exchangeRequestDTO.getQuote_amount() : exchangeRequestDTO.getBase_account());
        depositDTO.setDate(new Date(Instant.now().toEpochMilli()));

        return depositDTO;
    }
}
