package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.JwtToken;
import com.siuuuuu.backend.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenStoreService {
    private final JwtTokenRepository jwtTokenRepository;

    public void saveActiveToken(String jti, String subject, Instant expiresAt) {
        JwtToken token = JwtToken.builder()
                .jti(jti)
                .subject(subject)
                .expiresAt(expiresAt)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }

    public boolean isRevoked(String jti) {
        return jwtTokenRepository.findByJti(jti)
                .map(JwtToken::getRevoked)
                .orElse(true); // không thấy trong DB thì xem như không hợp lệ
    }

    public void revoke(String oldJti, String replacedBy) {
        jwtTokenRepository.findByJti(oldJti).ifPresent(t -> {
            t.setRevoked(true);
            t.setRevokedAt(Instant.now());
            t.setReplacedBy(replacedBy);
            jwtTokenRepository.save(t);
        });
    }

}
