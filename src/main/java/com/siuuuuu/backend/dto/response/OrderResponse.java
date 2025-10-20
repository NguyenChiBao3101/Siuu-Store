package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.OrderDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    @NotNull
    private List<OrderDetailResponse> orderDetails;
    @NotNull
    private OrderStatus status;
    @NotNull
    private int totalPrice;

    // Thông tin khách
    private String name;
    @Email
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private String address;
    private String note;

    // Thanh toán
    @NotNull
    private String paymentMethod; // "COD"/"VNPAY"/...
    @NotNull
    private String paymentStatus; //"PAID/UNPAID/PENDING/EXPIRED




}
