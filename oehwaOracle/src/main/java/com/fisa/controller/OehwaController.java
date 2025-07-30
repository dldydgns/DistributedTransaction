package com.fisa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping(value="/api/Oehwa")
@RequiredArgsConstructor 
public class OehwaController{
	private final MsiService msiService;
	private final Producer producer;
	// 입급
	@PostMapping("/deposit")
	public HttpStatus getOehwaDeposit(@RequestBody DepositDTO  dto) {
		msiService.setDepositOehwa(dto);
		return HttpStatus.OK;
	}
	
	// 출금
	@PostMapping("/withdrawal")
	public HttpStatus getOehwaWithdrawal(@RequestBody DepositDTO  dto) {
		msiService.setWithdrawal(dto);
		return HttpStatus.OK;
	}
	
	@GetMapping("/test")
	public String test() {
		producer.OehwaDeposit();
		return "테스트 Deposit 성도";
	}
	@GetMapping("/test2")
	public String test2() {
		producer.OehwaWithdrawal();
		return "테스트 Withdrawal 성공";
	}
}