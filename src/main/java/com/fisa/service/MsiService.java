package com.fisa.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;
import com.fisa.entity.UserBank;
import com.fisa.entity.Wonhwa;
import com.fisa.repository.UserBankRepository;
import com.fisa.repository.WonhwaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsiService {
    private final WonhwaRepository wonhwaRepository;
    private final UserBankRepository userBankRepository;

    public Wonhwa processTransaction(DepositDTO dto, boolean isDeposit) {
        String id = String.valueOf(dto.getAccountid());
        int account = dto.getAmount();

        UserBank userBank = userBankRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("UserBank with id '" + id + "' not found."));

        int newAmount = isDeposit
            ? userBank.getAccount() + dto.getAmount()
            : userBank.getAccount() - dto.getAmount();

        userBank.setAccount(newAmount);

        System.out.println("******************"+account);
        Wonhwa newWonhwa = Wonhwa.builder()
            .account(account) 
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
