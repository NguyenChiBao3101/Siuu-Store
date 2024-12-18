package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.siuuuuu.backend.dto.request.SignUpDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthService authService;


    @Value("${google.recaptcha.site-key}")
    private String siteKey;


    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("title", "Đăng Ký");
        model.addAttribute("signUpDto", new SignUpDto()); // Khởi tạo SignUpDto
        return "auth/sign-up";
    }


    @PostMapping("/sign-up")
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
            authService.signUp(signUpDto);
            model.addAttribute("title", "Đăng nhập");
            return "auth/sign-in";

        } catch (Exception e) {
            logger.error("Đăng ký thất bại", e);
            result.reject("error.global", "Đã xảy ra lỗi trong quá trình đăng ký");
            model.addAttribute("title", "Đăng Ký");
            return "auth/sign-up";
        }
    }


    @GetMapping("/sign-in")
    public String signIn(Model model) {
        model.addAttribute("siteKey", siteKey);
        model.addAttribute("title", "Đăng Nhập");
        return "auth/sign-in";
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        System.out.println("Token: " + token);
        try {
            authService.verifyAccount(token);
            return "auth/verify-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/verify-fail";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
