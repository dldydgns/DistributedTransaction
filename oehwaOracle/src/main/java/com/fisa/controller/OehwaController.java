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
        try {
            msiService.setDepositOehwa(dto);
            return ResponseEntity.ok("입금이 완료되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 비즈니스 에러 (ex. 계좌 없음, 잔액 부족 등)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 시스템 에러 (ex. DB 오류)
            return ResponseEntity.status(500).body("입금에 실패했습니다.");
        }
    }

    // 외화 출금
    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawOehwa(@RequestBody DepositDTO dto) {
        try {
            msiService.setWithdrawal(dto);
            return ResponseEntity.ok("출금이 완료되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("출금에 실패했습니다.");
        }
    }

    // 출금 성공 여부 체크 (GUID)
    @PostMapping("/check/withdrawal")
    public ResponseEntity<?> checkWithdrawal(@RequestBody Map<String, String> body) {
        try {
            String guid = body.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ResponseEntity.badRequest().body("GUID가 누락되었습니다.");
            }
            boolean result = msiService.isWithdrawalSuccess(guid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("출금 성공 여부 확인에 실패했습니다.");
        }
    }

    // 입금 성공 여부 체크 (GUID)
    @PostMapping("/check/deposit")
    public ResponseEntity<?> checkDeposit(@RequestBody Map<String, String> body) {
        try {
            String guid = body.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ResponseEntity.badRequest().body("GUID가 누락되었습니다.");
            }
            boolean result = msiService.isDepositSuccess(guid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("입금 성공 여부 확인에 실패했습니다.");
        }
    }
}
