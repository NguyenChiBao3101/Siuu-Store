package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.constant.PaymentMethod;
import com.siuuuuu.backend.constant.PaymentStatus;
import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    OrderHistoryService orderHistoryService;

    CartDetailService cartDetailService;

    AccountRepository accountRepository;

    // getOrdersByStatusAndUser
    public List<Order> getOrdersByStatusAndUser(OrderStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        return orderRepository.findByCustomerAndStatus(account, status);
    }

    // Get order with pagination
    public Page<Order> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.findAll(pageable);
    }


    public List<Order> searchOrdersById(String orderId) {
        return orderRepository.searchOrdersById(orderId);
    }

    public Page<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findOrdersByDateRange(startDate, endDate, pageable);
    }

    public Page<Order> findOrdersByStatusAndDateRange(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findOrdersByStatusAndDateRange(status, startDate, endDate, pageable);
    }

    public Page<Order> findOrdersByStatus(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findOrdersByStatus(status, pageable);
    }

    // Get order by current user and sort by created date
    public List<Order> getOrdersForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        return orderRepository.findByCustomerOrderByCreatedAtDesc(account);
    }

    // Get all orders and sort by created date
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order updateOrderWhenPaymentSuccess(String orderId, String transactionId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setVnpayTransactionNo(transactionId);
            order.setPaymentUrl(null);
            order.setPaymentExpirationDate(null);
            LocalDateTime paymentDate = LocalDateTime.now();
            order.setPaymentDate(paymentDate);
            orderRepository.save(order);
        }
        return order;
    }

    public void updateOrderWhenPaymentFailed(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentUrl(null);
            order.setPaymentExpirationDate(null);
            orderRepository.save(order);
        }
    }

    public Order createOrder(String name, String email, String address, String phone, int totalPrice, List<String> cartDetailIds, String paymentMethod) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);

        Order order = new Order();
        order.setShippingName(name);
        order.setShippingEmail(email);
        order.setShippingAddress(address);
        order.setShippingPhone(phone);
        order.setTotalPrice(totalPrice);
        order.setCustomer(account);
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
            order.setPaymentExpirationDate(LocalDateTime.now().plusMinutes(15));
            orderRepository.save(order);
        }
    }

    public void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderHistoryService.addOrderHistory(orderId, OrderStatus.CONFIRMED, account);
            orderRepository.save(order);
        }
    }

    public void shippingOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        if (order != null && order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.SHIPPING);
            orderHistoryService.addOrderHistory(orderId, OrderStatus.SHIPPING, account);
            orderRepository.save(order);
        }
    }

    public void completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        if (order != null && order.getStatus() == OrderStatus.SHIPPING) {
            order.setStatus(OrderStatus.COMPLETED);
            if (order.getPaymentMethod() == PaymentMethod.COD) {
                order.setPaymentStatus(PaymentStatus.PAID);
                order.setPaymentDate(LocalDateTime.now());
            }
            orderHistoryService.addOrderHistory(orderId, OrderStatus.COMPLETED, account);
            orderRepository.save(order);
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Account account = accountRepository.findByEmail(currentUserEmail);
        if (order != null && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setCanceledDate(LocalDateTime.now());
            orderHistoryService.addOrderHistory(orderId, OrderStatus.CANCELLED, account);
            orderRepository.save(order);
        }
    }
}
