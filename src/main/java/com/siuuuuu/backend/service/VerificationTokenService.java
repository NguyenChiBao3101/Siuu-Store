package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.VerificationToken;
import com.siuuuuu.backend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VerificationTokenService {
    VerificationTokenRepository verificationTokenRepository;

    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public String createVerificationToken(Account account) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setAccount(account);
        verificationToken.setToken(java.util.Base64.getEncoder().encodeToString(java.util.UUID.randomUUID().toString().getBytes()));
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
    }

    public void deleteVerificationToken(VerificationToken verificationToken) {
        VerificationToken token = verificationTokenRepository.findByToken(verificationToken.getToken());
        token.setToken("");
        verificationTokenRepository.save(token);
    }
}
