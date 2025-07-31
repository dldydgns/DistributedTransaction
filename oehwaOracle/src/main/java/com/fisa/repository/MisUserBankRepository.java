package com.fisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fisa.entity.UserBank;

public interface MisUserBankRepository extends JpaRepository<UserBank, String> {
}
