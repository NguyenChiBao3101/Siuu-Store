package com.siuuuuu.backend.controller.admin;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/profile")
public class ProfileController {
    @GetMapping("")
    public String getProfile(String id, Model model) {

        return "admin/profile/index";
    }
}
