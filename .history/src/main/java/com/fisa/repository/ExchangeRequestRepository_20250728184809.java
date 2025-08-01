package com.fisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fisa.msi.entity.ExchangeRequest;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {

}
