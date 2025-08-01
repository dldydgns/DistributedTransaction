package com.fisa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.dto.CheckOehwaAmountDTO;
import com.fisa.dto.CheckWonhwaAmountDTO;

@RestController
@RequestMapping("/check")
public class CheckAmountController {
    @GetMapping("/wonhwa")
    public CheckWonhwaAmountDTO getWonhwaExchange() {
        return OehwaCheck
    }

    @GetMapping("/oehwa")
    public CheckOehwaAmountDTO getOehwaExchange() {
        return OehwaCheck
    }
}
