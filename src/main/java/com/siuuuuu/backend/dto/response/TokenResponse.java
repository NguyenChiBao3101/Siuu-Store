package com.siuuuuu.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class TokenResponse {
    private String tokenType;       // Bearer
    private String accessToken;
    private Instant accessTokenExpiresAt;
    private String refreshToken;
    private Instant refreshTokenExpiresAt;

    // Thông tin tiện lợi cho client
    private String username;
    private Set<String> roles;
}
