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

@Configuration
@RequiredArgsConstructor
public class OehwaConsumer {
	private final MsiService msiService;
	
	@Bean
	public NewTopic OehwaDeposit() {
		return TopicBuilder.name("OehwaDeposit")
				.replicas(1)
				.build();
	}
	
	// 보상 입금
	@KafkaListener(topics = "ServerOehwaDeposit", groupId = "fisa")
	public void getOehwaDeposit(DepositDTO dto) throws IOException {
		System.out.println("consumer ::"+dto);
		msiService.setDepositOehwa(dto);
	}
	

	// 보상 출금
	@KafkaListener(topics = "ServerOehwaWithdrawal", groupId = "fisa")
	public void getWithdrawal(DepositDTO dto) throws IOException {
		System.out.println("consumer ::"+dto);
		msiService.setWithdrawal(dto);
	}
}









