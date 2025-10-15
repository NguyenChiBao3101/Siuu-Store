package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;@Entity
@Table(name = "jwt_tokens", indexes = {
        @Index(name = "idx_jwt_jti", columnList = "jti", unique = true),
        @Index(name = "idx_jwt_subject", columnList = "subject")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class JwtToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String jti;

    @Column(nullable = false, length = 255)
    private String subject;          // email/username

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Boolean revoked = false; // bị thu hồi hay chưa

    private String replacedBy;       // jti mới nếu đã rotate

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant revokedAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
