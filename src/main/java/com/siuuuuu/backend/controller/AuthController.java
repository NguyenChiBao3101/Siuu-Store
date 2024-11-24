package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.siuuuuu.backend.dto.request.SignUpDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("title", "Đăng Ký");
        model.addAttribute("signUpDto", new SignUpDto()); // Khởi tạo SignUpDto
        return "auth/sign-up";
    }


    @PostMapping("/sign-up")
    @Transactional
    public String signUp(@Valid SignUpDto signUpDto, BindingResult result, Model model) {
        logger.info("Post request received for sign-up");

        // Kiểm tra xác nhận mật khẩu
        if (!signUpDto.getPassword().equals(signUpDto.getConfirm_password())) {
            result.rejectValue("confirm_password", "error.signUpDto", "Mật khẩu không khớp");
        }

        // Kiểm tra lỗi ràng buộc
        if (result.hasErrors()) {
            logger.info("Validation errors: {}", result.getAllErrors());
            model.addAttribute("title", "Đăng Ký");
            return "auth/sign-up";
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (accountRepository.existsByEmail(signUpDto.getEmail())) {
            logger.warn("Email đã tồn tại: {}", signUpDto.getEmail());
            result.rejectValue("email", "error.signUpDto", "Email đã tồn tại");
            model.addAttribute("title", "Đăng Ký");
            return "auth/sign-up";
        }

        try {
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
            account.setRoles(roles);

            Account accountCreated = accountRepository.save(account);
            logger.info("Tài khoản được tạo thành công: {}", accountCreated);

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
            model.addAttribute("title", "Đăng Nhập");
            return "auth/sign-in";

        } catch (Exception e) {
            logger.error("Đăng ký thất bại", e);
            result.reject("error.global", "Đã xảy ra lỗi trong quá trình đăng ký");
            model.addAttribute("title", "Đăng Ký");
            return "auth/sign-up";
        }
    }


    @GetMapping("/sign-in")
    public String signIn(Model model ) {
        model.addAttribute("title", "Đăng Nhập");
        return "auth/sign-in";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
