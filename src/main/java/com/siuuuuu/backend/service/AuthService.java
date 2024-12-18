package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.controller.AuthController;
import com.siuuuuu.backend.dto.request.SignUpDto;
import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    AccountRepository accountRepository;

    ProfileRepository profileRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    CartRepository cartRepository;

    VerificationTokenService verificationTokenService;

    EmailService emailService;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        // Tạo tài khoản mới
        Account account = new Account();
        account.setEmail(signUpDto.getEmail());
        account.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Gán role mặc định là CUSTOMER
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(Roles.CUSTOMER.name());
        if (role != null) {
            roles.add(role);
        } else {
            logger.error("Role CUSTOMER không tồn tại trong database!");
            throw new RuntimeException("Role CUSTOMER không tồn tại");
        }
        account.setIsActive(false);
        account.setIsVerified(false);
        account.setRoles(roles);

        Account accountCreated = accountRepository.save(account);
        logger.info("Tài khoản được tạo thành công: {}", accountCreated);

        // Tạo mã xác thực
        String token = verificationTokenService.createVerificationToken(accountCreated);
        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
        emailService.sendVerificationEmail(accountCreated.getEmail(), "Xác thực tài khoản", verificationUrl);

        // Tạo profile mới liên kết với tài khoản
        Profile profile = new Profile();
        profile.setAccount(accountCreated);
        profile.setFirstName(signUpDto.getFirst_name());
        profile.setLastName(signUpDto.getLast_name());
        profile.setDateOfBirth(signUpDto.getDate_of_birth());
        profile.setIsActive(true);

        Profile profileCreated = profileRepository.save(profile);
        logger.info("Profile được tạo thành công: {}", profileCreated);

        // Tạo cart mới liên kết với tài khoản
        Cart cart = new Cart();
        cart.setAccount(accountCreated);
        Cart cartCreated = cartRepository.save(cart);
        logger.info("Cart được tạo thành công: {}", cartCreated);
    }

    public void verifyAccount(String token) {
        // Tìm mã xác thực trong database
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if (verificationToken == null) {
            throw new RuntimeException("Mã xác thực không tồn tại");
        }

        // Ma xac thuc da het han
        if (verificationToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Mã xác thực đã hết hạn");
        }

        // Tìm tài khoản liên kết với mã xác thực
        Account account = verificationToken.getAccount();
        if (account == null) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        // Xác thực tài khoản
        account.setIsVerified(true);
        account.setIsActive(true);
        accountRepository.save(account);

        // Xóa mã xác thực
        verificationTokenService.deleteVerificationToken(verificationToken);
    }
}