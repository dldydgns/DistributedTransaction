package com.fisa.msi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fisa.msi.entity.student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
