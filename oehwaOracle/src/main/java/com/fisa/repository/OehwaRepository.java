package com.fisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fisa.entity.Oehwa;
import java.util.Optional;

public interface OehwaRepository extends JpaRepository<Oehwa, Integer> {
    // guid로 Oehwa 단건 조회
    Optional<Oehwa> findByGuid(String guid);

    // userId 기준 최신 1개 거래 (MySQL용 LIMIT 1)
    @Query(
        value = "SELECT * FROM oehwa WHERE user_id = :userId ORDER BY create_at DESC LIMIT 1",
        nativeQuery = true
    )
    Optional<Oehwa> findLatestByUserId(@Param("userId") String userId);
}
