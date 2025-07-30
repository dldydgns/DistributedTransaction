package com.fisa.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisa.entity.DepositDTO;
import com.fisa.kafka.Producer;
import com.fisa.service.MsiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value="/api/wonhwa")
@RequiredArgsConstructor
public class WonhwaController {
	private final MsiService msiService;
	private final Producer producer;
	
	// 입급
	@PostMapping("/deposit")
	public HttpStatus getWonhwaDeposit(@RequestBody DepositDTO  dto) {
		msiService.setDepositWonhwa(dto);
		return HttpStatus.OK;
	}
	
	// 출금
	@PostMapping("/withdrawal")
	public HttpStatus getWonhwaWithdrawal(@RequestBody DepositDTO  dto) {
		msiService.setWithdrawal(dto);
		return HttpStatus.OK;
	}
	
	@GetMapping("/test")
	public String test() {
		producer.WonhwaDeposit();
		return "테스트 Deposit 성도";
	}
	@GetMapping("/test2")
	public String test2() {
		producer.WonhwaWithdrawal();
		return "테스트 Withdrawal 성공";
	}
	
}
