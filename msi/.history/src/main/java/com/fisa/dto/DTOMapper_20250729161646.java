package com.fisa.dto;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import com.fisa.entity.ExchangeRequest;

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

    public static ExchangeRequest toEntity(ExchangeRequestDTO dto) {
        return ExchangeRequest.builder()
            .userid(dto.getUserid())
            .direction(dto.getDirection())
            .base(dto.getBase())
            .base_account(dto.getBase_account())
            .base_amount(dto.getBase_amount())
            .quote(dto.getQuote())
            .quote_account(dto.getQuote_account())
            .quote_amount(dto.getQuote_amount())
            .rate(dto.getRate())
            .guid(dto.getGuid() != null ? dto.getGuid() : UUID.randomUUID())
            .status(dto.getStatus())
            .build();
    }
}
