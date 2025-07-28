package com.fisa.msi.dto;

public class ExchangeRequest {
    private String from;
    private String to;
    private int amount;

    // 기본 생성자
    public ExchangeRequest() {}

    // getter/setter
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
