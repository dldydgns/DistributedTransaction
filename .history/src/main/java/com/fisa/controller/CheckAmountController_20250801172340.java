package com.fisa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.dto.CheckAmountDTO;

@RestController
@RequestMapping("/check")
public class CheckAmountController {
    @GetMapping
    public CheckAmountDTO getExchange() {
        return ResponseEntity.ok("POST요청만 허용합니다.");
    }

}
