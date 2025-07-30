package com.fisa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;
import com.fisa.entity.UserBank;
import com.fisa.entity.Oehwa;
import com.fisa.repository.MisUserBankRepository;
import com.fisa.repository.OehwaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsiService {
	private final OehwaRepository ohewaRepository;
	private final MisUserBankRepository misuserBankRepository;
	
	public Oehwa setDepositOehwa(DepositDTO dto) {
		String id = String.valueOf(dto.getAccountid());
		
		UserBank userBank = misuserBankRepository.findById(id)
			    .orElseThrow(() -> new RuntimeException("해당 id의 UserBank를 찾을 수 없습니다."));

		userBank.setAccount(userBank.getAccount()+ dto.getAmount());
		Oehwa newOehwa = Oehwa.builder()
			    .account(dto.getAmount()) // 입금 금액
			    .createAt(LocalDateTime.now())
			    .type("입금")
			    .base(userBank.getBase())
			    .userBankId((String)userBank.getId()) 
			    .userId(id)
			    .build();
				
		
		misuserBankRepository.save(userBank);
		return ohewaRepository.save(newOehwa);
	}
	
	public Oehwa setWithdrawal(DepositDTO dto) {
		String id = String.valueOf(dto.getAccountid());
		
		UserBank userBank = misuserBankRepository.findById(id)
			    .orElseThrow(() -> new RuntimeException("해당 id의 UserBank를 찾을 수 없습니다."));

		userBank.setAccount(userBank.getAccount()- dto.getAmount());
		Oehwa newOehwa = Oehwa.builder()
			    .account(dto.getAmount()) // 입금 금액
			    .createAt(LocalDateTime.now())
			    .type("출금")
			    .base(userBank.getBase())
			    .userBankId((String)userBank.getId()) 
			    .build();
				
		
		misuserBankRepository.save(userBank);
		return ohewaRepository.save(newOehwa);
	}
	
}