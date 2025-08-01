package com.fisa.msi.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class exchange_request {
    private int id;
    private int user_id;
    private String direction;
    private String base;
    private String quote;
    private int base_amount;
    private int quote_amount;
    private double rate;
    private int guid;
}
