package com.fisa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fisa.entity.UserBank;
import com.fisa.dto.DepositDTO;
import com.fisa.entity.Oehwa;
import com.fisa.repository.MisUserBankRepository;
import com.fisa.repository.OehwaRepository;
import com.fisa.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsiService {
    private final OehwaRepository oehwaRepository;
    private final MisUserBankRepository misUserBankRepository;

    // 공통 트랜잭션 (입금/출금)
    public Oehwa processOehwaTransaction(DepositDTO dto, boolean isDeposit) {
        String id = String.valueOf(dto.getAccountid());
        int amount = dto.getAmount();

        // 1. 계좌 없음
        UserBank userBank = misUserBankRepository.findById(id)
            .orElseThrow(() -> new BusinessException("계좌가 존재하지 않습니다. (accountId=" + id + ")"));

        int newAmount = isDeposit
            ? userBank.getAccount() + amount
            : userBank.getAccount() - amount;

        // 2. 출금 시 잔액 부족
        if (!isDeposit && newAmount < 0) {
            throw new BusinessException("잔액이 부족합니다. (현재잔액=" + userBank.getAccount() + ", 요청금액=" + amount + ")");
        }

        userBank.setAccount(newAmount);

        // guid, status 추가
        String guid = (dto.getGuid() != null) ? dto.getGuid().toString() : null;

        Oehwa newOehwa = Oehwa.builder()
            .account(amount)
            .createAt(LocalDateTime.now())
            .type(isDeposit ? "입금" : "출금")
            .base(userBank.getBase())
            .userBankId(userBank.getId())
            .userId(id)
            .guid(guid)                 // guid 저장
            .build();

        misUserBankRepository.save(userBank);
        return oehwaRepository.save(newOehwa);
    }

    // 입금 처리
    public Oehwa setDepositOehwa(DepositDTO dto) {
        return processOehwaTransaction(dto, true);
    }

    // 출금 처리
    public Oehwa setWithdrawal(DepositDTO dto) {
        return processOehwaTransaction(dto, false);
    }

    // 입금 성공 여부 체크
    public boolean isDepositSuccess(String guid) {
		return oehwaRepository.findByGuid(guid).isPresent();
    }

    // 출금 성공 여부 체크
    public boolean isWithdrawalSuccess(String guid) {
		return oehwaRepository.findByGuid(guid).isPresent();
    }
}
