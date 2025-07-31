package com.fisa.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;
import com.fisa.entity.UserBank;
import com.fisa.entity.Wonhwa;
import com.fisa.repository.UserBankRepository;
import com.fisa.repository.WonhwaRepository;
import com.fisa.exception.BusinessException;  // 추가!

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsiService {
    private final WonhwaRepository wonhwaRepository;
    private final UserBankRepository userBankRepository;

    public Wonhwa processTransaction(DepositDTO dto, boolean isDeposit) {
        String id = String.valueOf(dto.getAccountid());
        int amount = dto.getAmount();

        // 1. 계좌 없음
        UserBank userBank = userBankRepository.findById(id)
            .orElseThrow(() -> new BusinessException("계좌가 존재하지 않습니다. (accountId=" + id + ")"));

        int newAmount = isDeposit
            ? userBank.getAccount() + amount
            : userBank.getAccount() - amount;

        // 2. 출금 시 잔액 부족
        if (!isDeposit && newAmount < 0) {
            throw new BusinessException("잔액이 부족합니다. (현재잔액=" + userBank.getAccount() + ", 요청금액=" + amount + ")");
        }

        userBank.setAccount(newAmount);

        System.out.println("******************" + amount);
        Wonhwa newWonhwa = Wonhwa.builder()
            .account(amount)
            .createAt(LocalDateTime.now())
            .type(isDeposit ? "입금" : "출금")
            .base(userBank.getBase())
            .userBankId(userBank.getId())
            .build();

        userBankRepository.save(userBank);
        return wonhwaRepository.save(newWonhwa);
    }

    // 입금 처리
    public Wonhwa setDepositWonhwa(DepositDTO dto) {
        return processTransaction(dto, true);
    }

    // 출금 처리
    public Wonhwa setWithdrawal(DepositDTO dto) {
        return processTransaction(dto, false);
    }
}
