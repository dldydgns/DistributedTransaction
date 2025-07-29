package com.fisa.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    private final ExchangeKafkaProducer producer;

    public ExchangeService(ExchangeKafkaProducer producer) {
        this.producer = producer;
    }

    public void requestExchange(String TOPIC, String requestJson) {
        producer.sendCompTx(TOPIC, requestJson);
    }
}
