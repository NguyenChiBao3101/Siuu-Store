package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class GlobalControllerAdvice {

    CartService cartService;

    @ModelAttribute
    public void addCartItemCountToModel(Model model) {
        int cartItemCount = cartService.getCartItemCountForCurrentUser();
        model.addAttribute("cartItemCount", cartItemCount);
    }
}
