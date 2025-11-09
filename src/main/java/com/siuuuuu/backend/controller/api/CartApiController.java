package com.siuuuuu.backend.controller.api;

import com.siuuuuu.backend.dto.request.CreateItemCartRequest;
import com.siuuuuu.backend.dto.request.UpdateQuantityRequest;
import com.siuuuuu.backend.dto.response.CartDetailResponse;
import com.siuuuuu.backend.dto.response.CartResponse;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.service.CartApiService;
import com.siuuuuu.backend.service.CartDetailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Validated
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartApiController {

    CartApiService cartApiService;
    @GetMapping("/{email}")
    public CartResponse getCartByAccount(@PathVariable String email) {
        return cartApiService.getCartByAccount(email);
    }

    @GetMapping("/{email}/details")
    public List<CartDetailResponse> getCartDetailByAccount(@PathVariable String email) {
        return cartApiService.getCartDetailsByEmail(email);
    }

    @GetMapping("/{email}/item/{cartDetailId}")
    public CartDetailResponse getItem(@PathVariable String cartDetailId, @PathVariable String email) {
        return cartApiService.getCartDetailById(cartDetailId, email);
    }

    /** Thêm sản phẩm vào giỏ (service trả về CartResponse) */
    @PostMapping("/{email}/add")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<CartResponse> addItem(@PathVariable String email, @Valid @RequestBody CreateItemCartRequest request) {
        CartResponse res = cartApiService.addProductToCartByEmail(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /** Cập nhật số lượng 1 dòng giỏ hàng */
    @PatchMapping("/{email}/quantity")
//    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public CartDetailResponse updateQuantity(
            @PathVariable String email,
            @Valid @RequestBody UpdateQuantityRequest request
    ) {
        cartApiService.updateProductQuantityInCart(email, request);
        return cartApiService.getCartDetailById(request.getCartDetailId(), email);
    }

    /** Xoá 1 dòng giỏ hàng */
    @DeleteMapping("/{email}/items/{cartDetailId}")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public MessageResponse removeItem(@PathVariable String cartDetailId,
                                      @PathVariable String email) {
        cartApiService.removeProductFromCart(cartDetailId,email);
        return new MessageResponse("Removed item: " + cartDetailId);
    }

    /** Xoá nhiều dòng giỏ hàng (bulk) */
    @DeleteMapping("/{email}/items")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public MessageResponse removeItems(@Valid @RequestBody RemoveItemsRequest request,
                                       @PathVariable String email) {
        cartApiService.removeItemsFromCart(email, request.getCartDetailIds());
        return new MessageResponse("Removed items: " + request.getCartDetailIds().size());
    }

    /** Xoá sạch giỏ hàng theo email */
    @DeleteMapping("/{email}/clear")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public MessageResponse clearCart(@PathVariable String email) {
        List<String> ids = cartApiService.getCartDetailsByEmail(email)
                .stream()
                .map(CartDetailResponse::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            cartApiService.removeItemsFromCart(email, ids);
        }
        return new MessageResponse("Cleared cart for: " + email);
    }


    @Data
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }

    @Data
    public static class RemoveItemsRequest {
        @NotEmpty(message = "Danh sách id không được trống")
        private List<@NotBlank String> cartDetailIds;
    }

}
