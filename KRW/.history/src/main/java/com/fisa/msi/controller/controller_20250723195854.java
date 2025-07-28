package com.fisa.msi.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class controller {

    @KafkaListener(topics = "USDtoKRW", groupId = "fisa")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}
