package com.fisa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.dto.CheckOehwaAmountDTO;
import com.fisa.dto.CheckWonhwaAmountDTO;

@RestController
@RequestMapping("/check")
public class CheckAmountController {
    @GetMapping(/wonhwa)
    public CheckWonhwaAmountDTO getExchange() {
        return ResponseEntity.ok("POST요청만 허용합니다.");
    }

    @GetMapping(/oehwa)
    public CheckOehwaAmountDTO getExchange() {
        return ResponseEntity.ok("POST요청만 허용합니다.");
    }
}
