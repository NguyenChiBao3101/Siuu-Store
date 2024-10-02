package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.dto.request.SignInDto;
import com.siuuuuu.backend.dto.response.ApiResponse;
import jakarta.validation.Valid;
import com.siuuuuu.backend.dto.request.SignUpDto;
import com.siuuuuu.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> signIn(@RequestBody @Valid SignInDto request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(request.getEmail(), request.getPassword()));
    }
}
