package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    
    private String type;
    private int amount;

    public Cash(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }
}
