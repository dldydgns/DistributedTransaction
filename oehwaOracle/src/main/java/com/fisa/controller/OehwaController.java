package com.fisa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fisa.dto.DepositDTO;
import com.fisa.service.MsiService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/oehwa")
@RequiredArgsConstructor
public class OehwaController {

    private final MsiService msiService;

    // 외화 입금
    @PostMapping("/deposit")
    public ResponseEntity<String> depositOehwa(@RequestBody DepositDTO dto) {
        msiService.setDepositOehwa(dto);
        return ResponseEntity.ok("입금 처리 성공");
    }

    // 외화 출금
    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawOehwa(@RequestBody DepositDTO dto) {
        msiService.setWithdrawal(dto);
        return ResponseEntity.ok("출금 처리 성공");
    }

    // 출금 성공 여부 체크 (GUID)
    @PostMapping("/check/withdrawal")
    public ResponseEntity<Boolean> checkWithdrawal(@RequestBody Map<String, String> body) {
        String guid = body.get("guid");
        boolean result = msiService.isWithdrawalSuccess(guid);
        return ResponseEntity.ok(result);
    }

    // 입금 성공 여부 체크 (GUID)
    @PostMapping("/check/deposit")
    public ResponseEntity<Boolean> checkDeposit(@RequestBody Map<String, String> body) {
        String guid = body.get("guid");
        boolean result = msiService.isDepositSuccess(guid);
        return ResponseEntity.ok(result);
    }
}
