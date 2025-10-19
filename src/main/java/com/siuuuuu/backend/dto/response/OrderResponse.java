package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {

    private final OrderStatus status;
    private final int shippingFee;
    private final int totalPrice;

    // Thông tin khách
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String note;

    // Thanh toán
    private final String paymentMethod; // "COD"/"VNPAY"/...
    private final String paymentStatus; //"PAID/UNPAID/PENDING/EXPIRED

    private final List<OrderDetailResponse> orderDetails;


}
