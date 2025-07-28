package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String type;
    private int amount;
}
