package com.siuuuuu.backend.service;

import java.util.List;

import com.siuuuuu.backend.repository.CartDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartService {

    AccountRepository accountRepository;

    CartDetailRepository cartDetailRepository;

    public int getCartItemCountForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() != "anonymousUser") {
            String currentUserEmail = authentication.getName();
            Account account = accountRepository.findByEmail(currentUserEmail);
            return account.getCart().getCartDetails().size();
        }
        return 0;
    }

    public List<CartDetail> getCartDetailsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        return account.getCart().getCartDetails();
    }

    public CartDetail getCartDetailById(String cartId) {
        return cartDetailRepository.findById(cartId).orElse(null);
    }
}