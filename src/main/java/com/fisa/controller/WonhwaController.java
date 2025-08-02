package com.fisa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fisa.dto.CheckAmountDTO;
import com.fisa.dto.DepositDTO;
import com.fisa.entity.Wonhwa;
import com.fisa.service.MsiService;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/wonhwa")
@RequiredArgsConstructor
public class WonhwaController {
    private final MsiService msiService;

    // 입금
    @PostMapping("/deposit")
    public ResponseEntity<String> depositWonhwa(@RequestBody DepositDTO dto) {
        try {
            msiService.setDepositWonhwa(dto);
            return ResponseEntity.ok("입금이 완료되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(299).body("입금 처리에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(299).body("입금 처리에 실패했습니다.");
        }
    }

    // 출금
    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawalWonhwa(@RequestBody DepositDTO dto) {
        try {
            msiService.setWithdrawal(dto);
            return ResponseEntity.ok("출금이 완료되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(299).body("출금 처리에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(299).body("출금 처리에 실패했습니다.");
        }
    }

    // 출금 성공 여부 체크 (GUID)
    @PostMapping("/check/withdrawal")
    public ResponseEntity<?> checkWithdrawal(@RequestBody Map<String, String> body) {
        try {
            String guid = body.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ResponseEntity.status(299).body("GUID가 누락되었습니다.");
            }
            if (msiService.existsWonhwaByGuid(guid)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(299).body("해당 GUID의 출금 기록이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(299).body("출금 성공 여부 확인에 실패했습니다.");
        }
    }

    // 입금 성공 여부 체크 (GUID)
    @PostMapping("/check/deposit")
    public ResponseEntity<?> checkDeposit(@RequestBody Map<String, String> body) {
        try {
            String guid = body.get("guid");
            if (guid == null || guid.isEmpty()) {
                return ResponseEntity.status(299).body("GUID가 누락되었습니다.");
            }
            if (msiService.existsWonhwaByGuid(guid)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(299).body("해당 GUID의 입금 기록이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(299).body("입금 성공 여부 확인에 실패했습니다.");
        }
    }

    @GetMapping("/check/wonhwa")
    public ResponseEntity<?> checkWonhwa(@RequestParam(value = "userid", defaultValue = "1") String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(299).body("userid가 누락되었습니다.");
        }
        Optional<Wonhwa> wonhwaOpt = msiService.findWonhwaByUserId(userId);
        if (wonhwaOpt.isPresent()) {
            Wonhwa wonhwa = wonhwaOpt.get();
            CheckAmountDTO dto = new CheckAmountDTO();
            dto.setUserid(wonhwa.getUserId());
            dto.setBase(wonhwa.getBase());
            dto.setBase_account(String.valueOf(wonhwa.getUserBankId())); // userBankId가 Wonhwa에 있다고 가정
            dto.setBase_amount(wonhwa.getAccount());
            return ResponseEntity.ok(dto); // DTO로 반환
        } else {
            return ResponseEntity.status(299).body("해당 userid의 원화 거래 내역이 없습니다.");
        }
    }

}
