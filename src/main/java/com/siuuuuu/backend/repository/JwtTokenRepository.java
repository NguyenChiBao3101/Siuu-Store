package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JwtTokenRepository extends JpaRepository<JwtToken, UUID> {
    Optional<JwtToken> findByJti(String jti);
}
