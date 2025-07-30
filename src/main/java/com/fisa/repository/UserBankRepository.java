package com.fisa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fisa.entity.UserBank;

public interface UserBankRepository extends JpaRepository<UserBank, String> {
}
