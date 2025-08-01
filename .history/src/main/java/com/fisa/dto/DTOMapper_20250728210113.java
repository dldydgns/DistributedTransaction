package com.fisa.dto;

import java.sql.Date;
import java.time.Instant;

public class DTOMapper {

    public static WithdrawalDTO toWithdrawalDTO(ExchangeRequestDTO exchangeRequestDTO, String direction) {
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

        withdrawalDTO.setUserid(exchangeRequestDTO.getUserid());
        withdrawalDTO.setAmount(direction.equals("BUY") ? exchangeRequestDTO.getBase_amount() : exchangeRequestDTO.getQuote_amount());
        withdrawalDTO.setAccountid(isBase ? exchangeRequestDTO.getBase_account() : exchangeRequestDTO.getQuote_account());

        // 현재 시간으로 세팅
        withdrawalDTO.setDate(new Date(Instant.now().toEpochMilli()));

        return withdrawalDTO;
    }

    public static DepositDTO toDepositDTO(ExchangeRequestDTO exchangeRequestDTO, boolean isBase) {
        DepositDTO depositDTO = new DepositDTO();

        depositDTO.setUserid(exchangeRequestDTO.getUserid());
        depositDTO.setAmount(isBase ? exchangeRequestDTO.getBase_amount() : exchangeRequestDTO.getQuote_amount());
        depositDTO.setAccountid(isBase ? exchangeRequestDTO.getBase_account() : exchangeRequestDTO.getQuote_account());

        // 현재 시간으로 세팅
        depositDTO.setDate(new Date(Instant.now().toEpochMilli()));

        return depositDTO;
    }
}
