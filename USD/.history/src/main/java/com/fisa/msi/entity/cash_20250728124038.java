package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String type;
    private int amount;

    Cash(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }
}
