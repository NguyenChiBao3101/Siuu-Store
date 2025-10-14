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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiController {

    private final AuthService authService;
    private final VerificationTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<SignUpDto> register(@Valid @RequestBody SignUpDto signUpDto) {
        authService.signUp(signUpDto); // trả về UserResponse hoặc info tối thiểu
        return ResponseEntity.ok(signUpDto);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authService.signIn(signInDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
