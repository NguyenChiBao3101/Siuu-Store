package com.siuuuuu.backend.service;

import java.util.List;

import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.repository.CartDetailRepository;
import com.siuuuuu.backend.repository.ProductVariantRepository;
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

    ProductVariantRepository productVariantRepository;

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
        Cart cart = account.getCart();
        List<CartDetail> cartDetails = cart.getCartDetails();
        cartDetails.sort((cartDetail1, cartDetail2) -> cartDetail2.getCreatedAt().compareTo(cartDetail1.getCreatedAt()));
        return cartDetails;
    }

    public CartDetail getCartDetailById(String cartId) {
        return cartDetailRepository.findById(cartId).orElse(null);
    }

    public void addProductToCart(String productVariantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        Cart cart = account.getCart();
        List<CartDetail> cartDetails = cart.getCartDetails();
        boolean isProductVariantExistInCart = false;
        for (CartDetail cartDetail : cartDetails) {
            if (cartDetail.getProductVariant().getId().equals(productVariantId)) {
                cartDetail.setQuantity(cartDetail.getQuantity() + 1);
                cartDetailRepository.save(cartDetail);
                isProductVariantExistInCart = true;
                break;
            }
        }
        if (!isProductVariantExistInCart) {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setCart(cart);
            cartDetail.setProductVariant(productVariantRepository.findById(productVariantId).orElse(null));
            cartDetail.setQuantity(1);
            cartDetailRepository.save(cartDetail);
        }
    }

    public void removeProductFromCart(String cartDetailId) {
        cartDetailRepository.deleteById(cartDetailId);
    }

    public void updateProductQuantityInCart(String cartDetailId, int quantity) {
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId).orElse(null);
        cartDetail.setQuantity(quantity);
        cartDetailRepository.save(cartDetail);
    }

    public void removeItemsFromCart(List<String> cartDetailIds) {
        cartDetailIds.forEach(cartDetailRepository::deleteById);
    }

}