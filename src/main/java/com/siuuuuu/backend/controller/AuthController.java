package com.siuuuuu.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.siuuuuu.backend.dto.request.SignUpDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("title", "Đăng Ký");
        model.addAttribute("signUpDto", new SignUpDto()); // Khởi tạo SignUpDto
        return "auth/sign-up";
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpDto signUpDto, BindingResult result, Model model) {
        logger.info("Post request received for sign-up");
        logger.info("SignUpDto: {}", signUpDto);
        logger.info("BindingResult: {}", result);
        if (!signUpDto.getPassword().equals(signUpDto.getConfirm_password())) {
            result.rejectValue("confirm_password", "error.signUpDto", "Mật khẩu không khớp");
        }
        if(result.hasErrors()) {
            model.addAttribute("title", "Đăng Ký"); // Thêm title vào mô hình
            return "auth/sign-up";
        }
        model.addAttribute("title", "Đăng Nhập");
        return "auth/sign-in";
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
