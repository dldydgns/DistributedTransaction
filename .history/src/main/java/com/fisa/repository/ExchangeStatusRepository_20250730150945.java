package com.fisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fisa.entity.ExchangeStatus;

public interface ExchangeStatusRepository extends JpaRepository<ExchangeStatus, Integer> {
    
}
