package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.controller.AuthController;
import com.siuuuuu.backend.dto.request.SignInDto;
import com.siuuuuu.backend.dto.request.SignUpDto;
import com.siuuuuu.backend.dto.response.TokenResponse;
import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    AccountRepository accountRepository;

    ProfileRepository profileRepository;

    RoleRepository roleRepository;

    CartRepository cartRepository;

    VerificationTokenService verificationTokenService;

    EmailService emailService;
    private final PasswordEncoder plainPasswordEncoder;

    JwtService jwtService;
    TokenStoreService tokenStoreService;

    public AuthService(AccountRepository accountRepository,
                       ProfileRepository profileRepository,
                       RoleRepository roleRepository,
                       CartRepository cartRepository,
                       VerificationTokenService verificationTokenService,
                       EmailService emailService,
                       @Qualifier("plainPasswordEncoder") PasswordEncoder plainPasswordEncoder,
                       JwtService jwtService,
                       TokenStoreService tokenStoreService) {
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.plainPasswordEncoder= plainPasswordEncoder;
        this.jwtService = jwtService;
        this.tokenStoreService = tokenStoreService;

        // DEBUG: in xem đang inject encoder nào
        logger.info("AuthService using PasswordEncoder: {}", plainPasswordEncoder.getClass().getName());
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        // Tạo tài khoản mới
        Account account = new Account();
        account.setEmail(signUpDto.getEmail());

        account.setPassword(plainPasswordEncoder.encode(signUpDto.getPassword()));

        // Gán role mặc định là CUSTOMER
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(Roles.CUSTOMER.name());
        if (role != null) {
            roles.add(role);
        } else {
            logger.error("Role CUSTOMER không tồn tại trong database!");
            throw new NoSuchElementException("Role CUSTOMER không tồn tại");
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
        profile.setPhoneNumber(signUpDto.getPhone_number());
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
            throw new NoSuchElementException("Mã xác thực không tồn tại");
        }

        // Ma xac thuc da het han
        if (verificationToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new BadCredentialsException("Mã xác thực đã hết hạn");
        }

        // Tìm tài khoản liên kết với mã xác thực
        Account account = verificationToken.getAccount();
        if (account == null) {
            throw new NoSuchElementException("Tài khoản không tồn tại");
        }

        // Xác thực tài khoản
        account.setIsVerified(true);
        account.setIsActive(true);
        accountRepository.save(account);

        // Xóa mã xác thực
        verificationTokenService.deleteVerificationToken(verificationToken);
    }

    public TokenResponse signIn(SignInDto req) {
        Account account = accountRepository.findByEmail(req.getUsername());
        if (account == null) throw new NoSuchElementException("Không tìm thấy tài khoản với email: " + req.getUsername());
        if (Boolean.FALSE.equals(account.getIsActive()))   throw new AccessDeniedException("Tài khoản đang bị vô hiệu hóa");
        if (Boolean.FALSE.equals(account.getIsVerified())) throw new AccessDeniedException("Tài khoản chưa được xác thực");
        if (!plainPasswordEncoder.matches(req.getPassword(), account.getPassword())) {
            throw new BadCredentialsException("Mật khẩu không đúng");
        }

        var roles = account.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet());
        var claims = new java.util.HashMap<String, Object>();
        claims.put("roles", roles);

        JwtService.IssuedToken issued = jwtService.issueAccessToken(account.getEmail(), claims);

        // lưu token đang hoạt động vào DB
        tokenStoreService.saveActiveToken(issued.jti(), account.getEmail(), issued.expiresAt());

        return TokenResponse.builder()
                .tokenType("Bearer")
                .accessToken(issued.token())
                .accessTokenExpiresAt(issued.expiresAt())
                .username(account.getEmail())
                .roles(roles)
                .build();
    }
    public TokenResponse refresh(String expiredAccessToken) {
        // 1) Phải là access token HẾT HẠN (nhưng chữ ký hợp lệ)
        if (!jwtService.isExpired(expiredAccessToken)) {
            throw new IllegalArgumentException("Access token chưa hết hạn; chưa cần refresh");
        }

        // 2) Lấy claims dù token đã hết hạn (đã verify chữ ký)
        var claims = jwtService.getClaimsAllowExpired(expiredAccessToken);
        String username = claims.getSubject();
        String oldJti   = claims.getId();
        Instant expAt   = claims.getExpiration().toInstant();

        // 3) Kiểm tra token cũ: chưa bị revoke và còn trong 'grace window'
        if (tokenStoreService.isRevoked(oldJti)) {
            throw new BadCredentialsException("Token đã bị thu hồi hoặc không hợp lệ");
        }
        if (!jwtService.withinRefreshGrace(expAt)) {
            throw new BadCredentialsException("Token đã hết hạn quá lâu, vui lòng đăng nhập lại");
        }

        // 4) Kiểm tra tài khoản
        Account account = accountRepository.findByEmail(username);
        if (account == null) throw new NoSuchElementException("Tài khoản không tồn tại");
        if (Boolean.FALSE.equals(account.getIsActive()))   throw new BadCredentialsException("Tài khoản đang bị vô hiệu hóa");
        if (Boolean.FALSE.equals(account.getIsVerified())) throw new BadCredentialsException("Tài khoản chưa được xác thực");

        // 5) Cấp access token mới
        var roles = account.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet());
        var newClaims = new HashMap<String, Object>();
        newClaims.put("roles", roles);

        var issued = jwtService.issueAccessToken(username, newClaims);

        // Lưu token mới và revoke token cũ (=> "xoá" hiệu lực token cũ)
        tokenStoreService.saveActiveToken(issued.jti(), username, issued.expiresAt());
        tokenStoreService.revoke(oldJti, issued.jti());

        return TokenResponse.builder()
                .tokenType("Bearer")
                .accessToken(issued.token())
                .accessTokenExpiresAt(issued.expiresAt())
                .username(username)
                .roles(roles)
                .build();
    }
}