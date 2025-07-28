package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int no;
    private String type;
    private int amount;

    public Cash(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }
}
