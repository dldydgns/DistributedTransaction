package com.fisa.msi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String name;
    private int age;
    private String mbti;
    private boolean glass;
}
