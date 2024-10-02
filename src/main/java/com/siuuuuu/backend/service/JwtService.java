package com.siuuuuu.backend.service;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .claim("scope", account.getRoles())
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claims);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to generate token");
        }
    }
}
