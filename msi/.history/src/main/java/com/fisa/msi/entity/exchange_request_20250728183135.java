package com.fisa.msi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
