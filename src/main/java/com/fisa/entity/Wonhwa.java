package com.fisa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Wonhwa {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int account; // 금액
	private LocalDateTime  createAt;
	private String type; // 입출급
	private String base; // 어느 국가인지
	private String userBankId; // 유저 뱅크 아이디
}
