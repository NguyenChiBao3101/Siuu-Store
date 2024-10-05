package com.siuuuuu.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign-up";
    }

    @GetMapping("/sign-in")
    public String signIn(Model model) {
        model.addAttribute("title", "Đăng Nhập");
        return "auth/sign-in";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
