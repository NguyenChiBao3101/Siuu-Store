package com.siuuuuu.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @GetMapping("")
    public String shop(Model model) {
        model.addAttribute("title", "Cửa hàng");
        return "shop/index";
    }
}
