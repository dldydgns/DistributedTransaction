package com.fisa.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.kafka.config.TopicBuilder;

import com.fisa.service.MsiService;
import com.fisa.dto.DepositDTO;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WonhwaConsumer {
    private final MsiService msiService;

    // 원화 입금 토픽 자동 생성
    @Bean
    public NewTopic WonhwaDeposit() {
        return TopicBuilder.name("ServerWonhwaDeposit")
                .replicas(1)
                .build();
    }

    // 원화 출금 토픽 자동 생성
    @Bean
    public NewTopic WonhwaWithdrawal() {
        return TopicBuilder.name("ServerWonhwaWithdrawal")
                .replicas(1)
                .build();
    }

    // 원화 입금 메시지 소비 (2회 재시도, DLQ 자동 이관, 지수 백오프)
    @KafkaListener(topics = "ServerWonhwaDeposit", groupId = "fisa")
    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = ".DLQ",
        autoCreateTopics = "true"
    )
    public void getWonhwaDeposit(DepositDTO dto) {
        System.out.printf(
            "Wonhwa Consumer [입금] guid: %s, userid: %s, accountid: %s, amount: %d, date: %s\n",
            dto.getGuid(), dto.getUserid(), dto.getAccountid(), dto.getAmount(), dto.getDate()
        );
        msiService.setDepositWonhwa(dto);
    }

    // 원화 출금 메시지 소비 (2회 재시도, DLQ 자동 이관, 지수 백오프)
    @KafkaListener(topics = "ServerWonhwaWithdrawal", groupId = "fisa")
    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(delay = 2000, multiplier = 2.0),
        dltTopicSuffix = ".DLQ",
        autoCreateTopics = "true"
    )
    public void getWonhwaWithdrawal(DepositDTO dto) {
        System.out.printf(
            "Wonhwa Consumer [출금] guid: %s, userid: %s, accountid: %s, amount: %d, date: %s\n",
            dto.getGuid(), dto.getUserid(), dto.getAccountid(), dto.getAmount(), dto.getDate()
        );
        msiService.setWithdrawal(dto);
    }
}
