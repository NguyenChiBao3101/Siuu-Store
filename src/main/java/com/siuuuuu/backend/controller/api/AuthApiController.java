package com.siuuuuu.backend.controller.api;

import com.siuuuuu.backend.dto.request.SignInDto;
import com.siuuuuu.backend.dto.request.SignUpDto;
import com.siuuuuu.backend.dto.response.TokenResponse;
import com.siuuuuu.backend.service.AuthService;
import com.siuuuuu.backend.service.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SignUpDto> register(@Valid @RequestBody SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        return ResponseEntity.ok(signUpDto);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authService.signIn(signInDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new BadCredentialsException("Token không hợp lệ");
        }
        String expiredAccessToken = auth.substring(7);
        return ResponseEntity.ok(authService.refresh(expiredAccessToken));
    }
}
