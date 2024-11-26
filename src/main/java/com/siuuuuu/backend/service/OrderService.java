package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.constant.PaymentMethod;
import com.siuuuuu.backend.constant.PaymentStatus;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderService {
    OrderRepository orderRepository;

    OrderDetailRepository orderDetailRepository;

    CartDetailService cartDetailService;

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order updateOrderWhenPaymentSuccess(String orderId, String transactionId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setVnpayTransactionNo(transactionId);
            order.setPaymentUrl(null);
            LocalDateTime paymentDate = LocalDateTime.now();
            order.setPaymentDate(paymentDate);
            orderRepository.save(order);
        }
        return order;
    }

    public Order updateOrderWhenPaymentFailed(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentUrl(null);
            orderRepository.save(order);
        }
        return order;
    }

    public Order createOrder(String name, String email, String address, String phone, int totalPrice, List<String> cartDetailIds, String paymentMethod) {
        Order order = new Order();
        order.setShippingName(name);
        order.setShippingEmail(email);
        order.setShippingAddress(address);
        order.setShippingPhone(phone);
        order.setTotalPrice(totalPrice);
        if (paymentMethod.equals("VN_PAY")) {
            order.setPaymentMethod(PaymentMethod.VN_PAY);
            order.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            order.setPaymentMethod(PaymentMethod.COD);
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }

        order = orderRepository.save(order);
        final Order finalOrder = order;

        List<CartDetail> cartDetails = cartDetailService.getCartDetailsByIds(cartDetailIds);
        List<OrderDetail> orderDetails = new ArrayList<>();
        cartDetails.forEach(cartDetail -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(finalOrder);
            orderDetail.setProductVariant(cartDetail.getProductVariant());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getProductVariant().getProduct().getPrice());
            orderDetail.setTotalPrice(cartDetail.getProductVariant().getProduct().getPrice() * cartDetail.getQuantity());
            // Save order detail
            orderDetails.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        });

        order.setOrderDetails(orderDetails);
        return order;
    }

    public void updatePaymentUrl(String orderId, String paymentUrl) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentUrl(paymentUrl);
            orderRepository.save(order);
        }
    }
}
