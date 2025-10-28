package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.request.CreateItemCartRequest;
import com.siuuuuu.backend.dto.request.UpdateQuantityRequest;
import com.siuuuuu.backend.dto.response.CartDetailResponse;
import com.siuuuuu.backend.dto.response.CartResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.entity.ProductVariant;
import com.siuuuuu.backend.exception.TooManyRequestsException;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartDetailRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartApiService {

    AccountRepository accountRepository;
    AccountService accountService;
    CartDetailRepository cartDetailRepository;
    ProductVariantRepository productVariantRepository;
    CartRepository cartRepository;
    CartDetailService cartDetailService;

    private Account getAccountOrThrow(String email) {
        Account acc = accountRepository.findByEmail(email);
        if (acc == null) {
            throw new NoSuchElementException("Account không tồn tại với email: " + email);
        }
        return acc;
    }

    private ProductVariant getVariantOrThrow(String productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new NoSuchElementException("ProductVariant không tồn tại: " + productVariantId));
    }

    public CartResponse getCartByAccount(String email) {
        Account account = getAccountOrThrow(email);
        return mapToDto(account.getCart());
    }

    /** Đếm số dòng (items) trong giỏ của 1 user (theo email) */
    public int getCartItemCountByEmail(String email) {
        Account account = getAccountOrThrow(email);
        Cart cart = account.getCart();
        return cart.getCartDetails() == null ? 0 : cart.getCartDetails().size();
    }

    /** Lấy danh sách CartDetail theo email, sắp xếp gần nhất trước */
    public List<CartDetailResponse> getCartDetailsByEmail(String email) {
        Account account = getAccountOrThrow(email);
        Cart cart = account.getCart();
        List<CartDetail> details = cart.getCartDetails();
        if (details != null) {
            details.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }
        assert details != null;
        return cartDetailService.mapToListDto(details);
    }

    /** Lấy CartDetail theo id (không phụ thuộc user) */
    private void assertOwnership(CartDetail d, String email) {
        String owner = (d.getCart() != null && d.getCart().getAccount() != null)
                ? d.getCart().getAccount().getEmail()
                : null;
        if (owner == null || !owner.equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("CartDetail không thuộc về tài khoản: " + email);
        }
    }

    /** Lấy CartDetail theo id (có kiểm tra ownership) */
    public CartDetailResponse getCartDetailById(String cartDetailId, String email) {
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new NoSuchElementException("CartDetail không tồn tại: " + cartDetailId));
        assertOwnership(cartDetail, email);
        return cartDetailService.mapToDto(cartDetail);
    }

    /**
     * Thêm sản phẩm vào giỏ của user theo email.
     * Nếu dòng đã tồn tại => cộng dồn quantity; nếu chưa => tạo mới.
     */
    @Transactional
    public CartResponse addProductToCartByEmail(String email, CreateItemCartRequest request) {
        Account account = getAccountOrThrow(email);
        Cart cart = account.getCart();
        ProductVariant variant = getVariantOrThrow(request.getProductVariantId());

        // Tìm dòng có sẵn theo variant
        CartDetail existed = null;
        if (cart.getCartDetails() != null) {
            for (CartDetail d : cart.getCartDetails()) {
                if (d.getProductVariant() != null &&
                        variant.getId().equals(d.getProductVariant().getId())) {
                    existed = d;
                    break;
                }
            }
        }

        int adding = request.getQuantity();
        if (existed != null) {
            int newQty = existed.getQuantity() + adding;
            //check kho
            if (newQty > variant.getQuantity()) throw new TooManyRequestsException("Không đủ hàng trong kho", 30L);
            existed.setQuantity(newQty);
        } else {
            if (adding > variant.getQuantity()) {
                throw new TooManyRequestsException("Không đủ hàng trong kho", 30L);
            }
            CartDetail newDetail = new CartDetail();
            newDetail.setCart(cart);
            newDetail.setProductVariant(variant);
            newDetail.setQuantity(adding);

            cart.addDetail(newDetail);
        }
        cartRepository.save(cart);
        return mapToDto(cart);
    }

    /** Cập nhật quantity 1 dòng giỏ hàng theo cartDetailId */
    @Transactional
    public void updateProductQuantityInCart(String email, UpdateQuantityRequest request) {
        CartDetail d = cartDetailRepository.findById(request.getCartDetailId())
                .orElseThrow(() -> new NoSuchElementException("CartDetail không tồn tại: " + request.getCartDetailId()));
        assertOwnership(d, email);
        d.setQuantity(request.getQuantity());
        cartDetailRepository.save(d);
    }

    /** Xoá 1 dòng giỏ hàng theo cartDetailId */
    @Transactional
    public void removeProductFromCart(String cartDetailId, String email) {
        CartDetail d = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new NoSuchElementException("CartDetail không tồn tại: " + cartDetailId));
        assertOwnership(d, email);
        cartDetailRepository.delete(d);
    }
    /** Xoá nhiều dòng giỏ hàng */
    @Transactional
    public void removeItemsFromCart(String email, List<String> cartDetailIds) {
        if (cartDetailIds == null || cartDetailIds.isEmpty()) return;
        for (String id : cartDetailIds) {
            cartDetailRepository.findById(id).ifPresent(d -> {
                assertOwnership(d, email);
                cartDetailRepository.delete(d);
            });
        }
    }

    public CartResponse mapToDto(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setAccountDtoResponse(accountService.mapToDto(cart.getAccount()));
        cartResponse.setCreatedAt(cart.getCreatedAt());
        cartResponse.setCartDetails(cartDetailService.mapToListDto(cart.getCartDetails()));
        return  cartResponse;
    }
    public List<CartResponse> mapToListDto(List<Cart> carts) {
        List<CartResponse> responseList = new ArrayList<>();
        for(Cart cart : carts) {
            CartResponse cartResponse = mapToDto(cart);
            responseList.add(cartResponse);
        }
        return responseList;
    }
}
