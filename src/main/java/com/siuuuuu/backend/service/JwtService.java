package com.siuuuuu.backend.service;

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

@Service
public class JwtService {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private long accessTtlMs;
    private long refreshTtlMs;

    public JwtService(
            @Value("${app.jwt.private-key}") String privateKeyPem,
            @Value("${app.jwt.public-key}") String publicKeyPem,
            @Value("${app.jwt.access-ttl-ms:900000}") long accessTtlMs,        // 15m
            @Value("${app.jwt.refresh-ttl-ms:604800000}") long refreshTtlMs    // 7d
    ) throws Exception {
        this.privateKey = parsePrivateKey(privateKeyPem);
        this.publicKey = parsePublicKey(publicKeyPem);
        this.accessTtlMs = accessTtlMs;
        this.refreshTtlMs = refreshTtlMs;
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

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(accessTtlMs)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshTtlMs)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
}
