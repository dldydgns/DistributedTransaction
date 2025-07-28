package com.fisa.msi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {
    @GetMapping("/exchange")
    public String getExchange() {
        return "Hello World";
    }
}
