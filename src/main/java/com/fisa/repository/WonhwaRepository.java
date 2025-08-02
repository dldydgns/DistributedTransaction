package com.fisa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.fisa.entity.Wonhwa;

public interface WonhwaRepository extends JpaRepository<Wonhwa, Integer> {
    // guid로 Wonhwa 단건 조회
    Optional<Wonhwa> findByGuid(String guid);

    // userid로 최신 1건 조회 (MySQL LIMIT 사용!)
    @Query(
        value = "SELECT * FROM wonhwa WHERE user_id = :userId ORDER BY create_at DESC LIMIT 1",
        nativeQuery = true
    )
    Optional<Wonhwa> findLatestByUserId(@Param("userId") String userId);
}
