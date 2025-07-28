package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cash {
    private int no;
    private String type;
    private int amount;

    public Cash(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }
}
