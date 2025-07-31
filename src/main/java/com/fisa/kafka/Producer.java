package com.fisa.kafka;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fisa.entity.DepositDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, DepositDTO> kafkaTemplate;

    public void WonhwaDeposit() {
        DepositDTO dto = new DepositDTO(
            UUID.randomUUID(), // guid
            "1",               // userid
            "1",               // accountid
            1000,              // amount
            LocalDateTime.now(), // date
            null                // retryCount (초기 null 또는 0)
        );
        System.out.println("Producer topic: ServerWonhwaDeposit, data: " + dto);

        kafkaTemplate.send("ServerWonhwaDeposit", dto);
    }

    public void WonhwaWithdrawal() {
        DepositDTO dto = new DepositDTO(
            UUID.randomUUID(),
            "1",
            "1",
            1000,
            LocalDateTime.now(),
            null               // retryCount (초기 null 또는 0)
        );
        System.out.println("Producer topic: ServerWonhwaWithdrawal, data: " + dto);

        kafkaTemplate.send("ServerWonhwaWithdrawal", dto);
    }
}
