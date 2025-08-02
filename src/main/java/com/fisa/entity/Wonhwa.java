package com.fisa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
public class Wonhwa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wonhwa_seq_gen")
    @SequenceGenerator(name = "wonhwa_seq_gen", sequenceName = "WONHWA_SEQ", allocationSize = 1)
    private int id;
    private String guid;           // GUID
    private int account;           // 금액
    private String userId;         // 사용자ID
    private String type;           // 입금/출금 타입
    private String base;           // 통화(예: KRW, USD)
    private LocalDateTime createAt;// 생성시각
    private String userBankId;     // 유저뱅크ID
}
