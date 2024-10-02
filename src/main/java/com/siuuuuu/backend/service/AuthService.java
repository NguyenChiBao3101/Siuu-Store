package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.dto.response.ApiResponse;
import com.siuuuuu.backend.dto.response.AuthResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.exception.AppException;
import com.siuuuuu.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public ApiResponse<AuthResponse> signUp(String email, String password) {
        if (accountRepository.existsByEmail(email)) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Email already exists");
        }
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRoles(Roles.CUSTOMER);
        accountRepository.save(account);
        String accessToken = jwtService.generateToken(account);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Success", new AuthResponse(account.getId(), account.getEmail(), accessToken));
    }

    public ApiResponse<AuthResponse> signIn(String email, String password) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST.value(), "Email or Password is incorrect"));
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Email or Password is incorrect");
        }
        String accessToken = jwtService.generateToken(account);
        return new ApiResponse<>(HttpStatus.OK.value(), "Success", new AuthResponse(account.getId(), account.getEmail(), accessToken));
    }
}
