package com.fisa.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Producer {
	 private final KafkaTemplate<String, DepositDTO> kafkaTemplate;

	    public void WonhwaDeposit() {
	    	DepositDTO dto = new DepositDTO("1", "1", 1000, LocalDateTime.now());
	    	System.out.println("Producer topic: WonhwaDeposit11111, data: " + dto);
	    	
	        kafkaTemplate.send("ServerWonhwaDeposit", dto);
	    }
	    
	    public void WonhwaWithdrawal() {
	    	DepositDTO dto = new DepositDTO("1", "1", 1000, LocalDateTime.now());
	    	System.out.println("Producer topic: WonhwaWithdrawal22222, data: " + dto);
	    	
	        kafkaTemplate.send("ServerWonhwaWithdrawal", dto);
	    }
}
