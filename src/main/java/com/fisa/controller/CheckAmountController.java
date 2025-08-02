package com.fisa.controller;

import com.fisa.dto.CheckAmountDTO;
import com.fisa.entity.Wonhwa;
import com.fisa.service.MsiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/checkamount")
@RequiredArgsConstructor
public class CheckAmountController {

    private final MsiService msiService;

    // 원화 환전 금액 조회
    @GetMapping
    public CheckAmountDTO getWonhwaAmount() {
        // 예시: "1"번 userid 기준. 상황에 맞게 파라미터로 변경 가능
        Optional<Wonhwa> wonhwaOpt = msiService.findWonhwaByUserId("1");
        if (wonhwaOpt.isPresent()) {
            Wonhwa wonhwa = wonhwaOpt.get();
            CheckAmountDTO dto = new CheckAmountDTO();
            dto.setUserid(wonhwa.getUserId());
            dto.setBase(wonhwa.getBase());
            dto.setBase_account(String.valueOf(wonhwa.getUserBankId()));
            dto.setBase_amount(wonhwa.getAccount());
            return dto;
        } else {
            // 없는 경우 예외처리 또는 null/기본값 반환(적절히 조정)
            throw new RuntimeException("해당 userid의 원화 환전 내역이 없습니다.");
        }
    }
}
