package com.siuuuuu.backend.controller.client;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ui.Model;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartController {

    CartService cartService;

    @RequestMapping("")
    public String index(Model model) {
        List<CartDetail> cartDetails = cartService.getCartDetailsForCurrentUser();
        model.addAttribute("title", "Giỏ Hàng");
        model.addAttribute("cartDetails", cartDetails);
        return "cart/index";
    }
}
