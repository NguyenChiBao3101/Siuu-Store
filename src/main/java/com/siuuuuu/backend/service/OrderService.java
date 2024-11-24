package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.PaymentMethod;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderService {
    OrderRepository orderRepository;

    CartDetailService cartDetailService;

    public Order createOrder(String name, String email, String address, String phone, int totalPrice, List<String> cartDetailIds, String paymentMethod) {
        Order order = new Order();
        order.setShippingName(name);
        order.setShippingEmail(email);
        order.setShippingAddress(address);
        order.setShippingPhone(phone);
        order.setTotalPrice(totalPrice);
        if (paymentMethod.equals("VN_PAY")) {
            order.setPaymentMethod(PaymentMethod.VN_PAY);
        } else {
            order.setPaymentMethod(PaymentMethod.COD);
        }
        // Save order to get order id
        order = orderRepository.save(order);
        final Order finalOrder = order;

        List<CartDetail> cartDetails = cartDetailService.getCartDetailsByIds(cartDetailIds);
        cartDetails.forEach(cartDetail -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(finalOrder);
            orderDetail.setProductVariant(cartDetail.getProductVariant());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getProductVariant().getProduct().getPrice());

            });
        return orderRepository.save(order);
    }
}
