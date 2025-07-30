package com.fisa.kafka;

import java.io.IOException;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;
import com.fisa.service.MsiService;

import lombok.RequiredArgsConstructor;


// 보상 트랙지션
@Configuration
@RequiredArgsConstructor
public class WonhwaConsumer {
	private final MsiService msiService;
	
	@Bean
	public NewTopic WonhwaDeposit() {
		return TopicBuilder.name("ServerWonhwaDeposit")
				.replicas(1)
				.build();
	}
	
	// 보상 입금
	@KafkaListener(topics = "ServerWonhwaDeposit", groupId = "fisa")
	public void getWonhwaDeposit(DepositDTO dto) throws IOException {
		System.out.println("consumer ::"+dto);
		msiService.setDepositWonhwa(dto);
	}
	
	
	@Bean
	public NewTopic WonhwaWithdrawal() {
		return TopicBuilder.name("ServerWonhwaWithdrawal")
				.replicas(1)
				.build();
	}

	// 보상 출금
	@KafkaListener(topics = "ServerWonhwaWithdrawal", groupId = "fisa")
	public void getWithdrawal(DepositDTO dto) throws IOException {
		System.out.println("consumer ::"+dto);
		msiService.setWithdrawal(dto);
	}
}