package com.fisa.controller;

import com.fisa.dto.CheckAmountDTO;
import com.fisa.entity.Oehwa;
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

    @GetMapping
    public CheckAmountDTO getOehwaAmount() {
        Optional<Oehwa> oehwaOpt = msiService.findOehwaByUserId("1");
        if (oehwaOpt.isPresent()) {
            Oehwa oehwa = oehwaOpt.get();
            CheckAmountDTO dto = new CheckAmountDTO();
            dto.setUserid(oehwa.getUserId());
            dto.setBase(oehwa.getBase());
            dto.setBase_account(String.valueOf(oehwa.getUserBankId()));
            dto.setBase_amount(oehwa.getAccount());
            return dto;
        } else {
            throw new RuntimeException("해당 userid의 외화 환전 내역이 없습니다.");
        }
    }
}
