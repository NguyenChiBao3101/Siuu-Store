package com.siuuuuu.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("title", "Trang Quản Trị");
        return "admin/index";
    }

    @GetMapping("/category")
    public String category(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Quản Lý Danh Mục");
        return "admin/category";
    }
}
