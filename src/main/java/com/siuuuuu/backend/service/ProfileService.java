package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.request.UpdateProfileDto;
import com.siuuuuu.backend.dto.response.ProfileDtoResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProfileService {
    AccountRepository accountRepository;

    PasswordEncoder passwordEncoder;

    public Profile getProfileCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        return account.getProfile();
    }

    public Profile updateProfile(UpdateProfileDto profile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        Profile currentProfile = account.getProfile();
        currentProfile.setFirstName(profile.getFirstName());
        currentProfile.setLastName(profile.getLastName());
        currentProfile.setPhoneNumber(profile.getPhoneNumber());
        currentProfile.setDateOfBirth(profile.getDateOfBirth());
        accountRepository.save(account);
        return account.getProfile();
    }


    public void updatePassword(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        // Check current password
        if (passwordEncoder.matches(password, account.getPassword())) {
            // Update new password
            account.setPassword(passwordEncoder.encode(password));
            accountRepository.save(account);
        } else {
            throw new BadCredentialsException("Mật khẩu không đúng");
        }
    }

    public ProfileDtoResponse mapToDto(Profile profile) {
        ProfileDtoResponse profileDtoResponse = new ProfileDtoResponse();
        profileDtoResponse.setFirstName(profile.getFirstName());
        profileDtoResponse.setLastName(profile.getLastName());
        profileDtoResponse.setDateOfBirth(profile.getDateOfBirth());
        profileDtoResponse.setPhoneNumber(profile.getPhoneNumber());
        return profileDtoResponse;
    }
}
