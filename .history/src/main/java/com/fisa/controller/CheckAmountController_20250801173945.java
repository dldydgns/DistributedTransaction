package com.fisa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.dto.CheckAmountDTO;
import com.fisa.dto.CheckAmountDTO;
import com.fisa.service.ExchangeService;

@RestController
@RequestMapping("/check")
public class CheckAmountController {

    private final ExchangeService exchangeService;

    public CheckAmountController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }
    
    @GetMapping("/wonhwa")
    public CheckAmountDTO getWonhwaExchange() {
        return exchangeService.OehwaCheck();
    }

    @GetMapping("/oehwa")
    public CheckAmountDTO getOehwaExchange() {
        return exchangeService.OehwaCheck();
    }
}
