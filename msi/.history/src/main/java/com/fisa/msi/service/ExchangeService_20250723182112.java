package com.fisa.msi.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;

    public ExchangeService(ExchangeKafkaProducer producer) {
        this.producer = producer;
    }

    public void requestExchange(String requestJson) {
        // 필요한 환전 로직 처리 후 Kafka 메시지 발송
        producer.sendExchangeRequest(requestJson);
    }
}
