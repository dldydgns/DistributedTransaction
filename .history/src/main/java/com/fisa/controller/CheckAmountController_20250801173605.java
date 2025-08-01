package com.fisa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.dto.CheckOehwaAmountDTO;
import com.fisa.dto.CheckWonhwaAmountDTO;
import com.fisa.service.ExchangeService;

@RestController
@RequestMapping("/check")
public class CheckAmountController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }
    
    @GetMapping("/wonhwa")
    public CheckWonhwaAmountDTO getWonhwaExchange() {
        return exchangeService.OehwaCheck();
    }

    @GetMapping("/oehwa")
    public CheckOehwaAmountDTO getOehwaExchange() {
        return exchangeService.OehwaCheck();
    }
}
