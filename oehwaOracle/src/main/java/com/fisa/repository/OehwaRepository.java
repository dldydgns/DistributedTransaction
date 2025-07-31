package com.fisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fisa.entity.Oehwa;
import java.util.Optional;

public interface OehwaRepository extends JpaRepository<Oehwa, Integer> {
    // guid와 type("입금" 또는 "출금")으로 Oehwa 단건 조회
    Optional<Oehwa> findByGuid(String guid);
}
