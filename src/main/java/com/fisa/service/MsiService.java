package com.fisa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fisa.dto.DepositDTO;
import com.fisa.entity.UserBank;
import com.fisa.entity.Wonhwa;
import com.fisa.repository.UserBankRepository;
import com.fisa.repository.WonhwaRepository;
import com.fisa.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsiService {
    private final WonhwaRepository wonhwaRepository;
    private final UserBankRepository userBankRepository;

    // 공통 트랜잭션 (입금/출금)
    public Wonhwa processTransaction(DepositDTO dto, boolean isDeposit) {
        String accountId = dto.getAccountid();
        int amount = dto.getAmount();

        // 1. 계좌 존재 여부 확인
        UserBank userBank = userBankRepository.findById(accountId)
            .orElseThrow(() -> new BusinessException("계좌가 존재하지 않습니다. (accountId=" + accountId + ")"));

        int newAmount = isDeposit
            ? userBank.getAccount() + amount
            : userBank.getAccount() - amount;

        // 2. 출금 시 잔액 부족 체크
        if (!isDeposit && newAmount < 0) {
            throw new BusinessException("잔액이 부족합니다. (현재잔액=" + userBank.getAccount() + ", 요청금액=" + amount + ")");
        }

        userBank.setAccount(newAmount);

        Wonhwa newWonhwa = Wonhwa.builder()
            .account(amount)
            .createAt(LocalDateTime.now())
            .type(isDeposit ? "입금" : "출금")
            .base(userBank.getBase())
            .userBankId(userBank.getId())
            .userId(dto.getUserid())
            .guid(dto.getGuid() != null ? dto.getGuid().toString() : null)
            .build();

        // 저장 (원자성 보장: userBank → wonhwa)
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

    // GUID로 Wonhwa 기록 존재 여부 체크 (값 반환 없음)
    public boolean existsWonhwaByGuid(String guid) {
        return wonhwaRepository.findByGuid(guid).isPresent();
    }

    public Optional<Wonhwa> findWonhwaByGuid(String guid) {
    return wonhwaRepository.findByGuid(guid);
}

public Optional<Wonhwa> findWonhwaByUserId(String userId) {
    return wonhwaRepository.findLatestByUserId(userId); // 네이티브 쿼리 방식 추천(11g XE)
}



}
