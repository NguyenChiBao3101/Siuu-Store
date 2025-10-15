package com.siuuuuu.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private long accessTtlMs;
    private final long refreshGraceMs;

    public JwtService(
            @Value("${app.jwt.private-key}") String privateKeyPem,
            @Value("${app.jwt.public-key}") String publicKeyPem,
            @Value("${app.jwt.access-ttl-ms}") long accessTtlMs,        // 10m
            @Value("${app.jwt.refresh-grace-ms}") long refreshGraceMs   //30m

    ) throws Exception {
        this.privateKey = parsePrivateKey(privateKeyPem);
        this.publicKey = parsePublicKey(publicKeyPem);
        this.accessTtlMs = accessTtlMs;
        this.refreshGraceMs = refreshGraceMs;
    }

    private PrivateKey parsePrivateKey(String pem) throws Exception {
        String clean = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] bytes = Base64.getDecoder().decode(clean);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    private PublicKey parsePublicKey(String pem) throws Exception {
        String clean = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] bytes = Base64.getDecoder().decode(clean);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }

    public IssuedToken issueAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString();
        Instant exp = now.plusMillis(accessTtlMs);

        String token = Jwts.builder()
                .setId(jti)
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        return new IssuedToken(token, jti, exp);
    }

    public boolean isValidAndNotExpired(String token) {
        try {
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
            return true; // hợp lệ & chưa hết hạn
        } catch (ExpiredJwtException e) {
            return false; // đã hết hạn
        } catch (JwtException | IllegalArgumentException e) {
            return false; // sai chữ ký / format
        }
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            // không hợp lệ khác -> coi như expired cho luồng refresh sẽ fail sớm
            return true;
        }
    }

    /** Lấy claims ngay cả khi token đã hết hạn (đã verify chữ ký). */
    public Claims getClaimsAllowExpired(String token) {
        try {
            return Jwts.parser().verifyWith(publicKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // chữ ký hợp lệ nhưng exp
        }
    }

    public String getJti(String token) {
        try {
            return Jwts.parser().verifyWith(publicKey).build()
                    .parseSignedClaims(token).getPayload().getId();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getId();
        }
    }

    public String getUsername(String token) {
        try {
            return Jwts.parser().verifyWith(publicKey).build()
                    .parseSignedClaims(token).getPayload().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    /** Cho phép refresh trong "grace window" sau khi hết hạn. */
    public boolean withinRefreshGrace(Instant tokenExp) {
        return Instant.now().isBefore(tokenExp.plusMillis(refreshGraceMs));
    }

    public record IssuedToken(String token, String jti, Instant expiresAt) {}
}
