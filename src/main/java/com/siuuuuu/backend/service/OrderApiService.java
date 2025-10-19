package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.constant.PaymentMethod;
import com.siuuuuu.backend.constant.PaymentStatus;
import com.siuuuuu.backend.dto.request.CreateOrderRequest;
import com.siuuuuu.backend.dto.request.CreateProductRequest;
import com.siuuuuu.backend.dto.response.OrderDetailResponse;
import com.siuuuuu.backend.dto.response.OrderResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderApiService {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private CartDetailService cartDetailService;
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        return mapToListDto(orders);
    }
    public List<OrderResponse> getAllOrdersAccount(String email) {
        Account account = accountRepository.findByEmail(email);
        if(account == null) {
            throw new NoSuchElementException("Tài khoản không tồn tại");
        }
        List<Order> orders = orderRepository.findByCustomerOrderByCreatedAtDesc(account);
        return mapToListDto(orders);
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findOneById(orderId);
        if(order == null) {
            throw new NoSuchElementException("Đơn hàng không tồn tại");
        }
        return mapToDto(order);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        Account account = accountRepository.findByEmail(request.getEmail());
        if(account == null) {
            throw new NoSuchElementException("Tài khoản không tồn tại");
        }
        Order order = new Order();
        order.setShippingName(request.getName());
        order.setShippingEmail(request.getEmail());
        order.setShippingAddress(request.getAddress());
        order.setShippingPhone(request.getPhone());
        order.setTotalPrice(request.getTotalPrice());
        order.setCustomer(account);
        if (request.getPaymentMethod().equals("VN_PAY")) {
            order.setPaymentMethod(PaymentMethod.VN_PAY);
            order.setPaymentStatus(PaymentStatus.PENDING);

        } else {
            order.setPaymentMethod(PaymentMethod.COD);
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }

        order = orderRepository.save(order);
        final Order finalOrder = order;

        List<CartDetail> cartDetails = cartDetailService.getCartDetailsByIds(request.getCartDetailIds());
        List<OrderDetail> responseList = new ArrayList<>();
        cartDetails.forEach(cartDetail -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(finalOrder);
            orderDetail.setProductVariant(cartDetail.getProductVariant());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getProductVariant().getProduct().getPrice());
            orderDetail.setTotalPrice(cartDetail.getProductVariant().getProduct().getPrice() * cartDetail.getQuantity());
            // Save order detail
            responseList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        });
        order.setOrderDetails(responseList);
        return mapToDto(order);
    }

    public void confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        assert order != null;
        Account account = order.getCustomer();
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderHistoryService.addOrderHistory(orderId, OrderStatus.CONFIRMED, account);
            orderRepository.save(order);
        }
    }

    public void shippingOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        assert order != null;
        Account account = order.getCustomer();
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.SHIPPING);
            orderHistoryService.addOrderHistory(orderId, OrderStatus.SHIPPING, account);
            orderRepository.save(order);
        }
    }

    public void completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        assert order != null;
        Account account = order.getCustomer();
        if (order.getStatus() == OrderStatus.SHIPPING) {
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


    public OrderResponse mapToDto(Order order) {
        if (order == null)  {
            throw new NoSuchElementException("Đơn đặt hàng không tồn tại");
        }

        return OrderResponse.builder()
                .status(order.getStatus())
                .orderDetails(orderDetailService.mapToListDto(order.getOrderDetails()))
                .totalPrice(order.getTotalPrice())
                // Thông tin khách
                .name(order.getShippingName())
                .email(order.getShippingEmail())
                .phone(order.getShippingPhone())
                .address(order.getShippingAddress())
                .note(order.getShippingNote())
                // Thanh toán
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .build();
    }

    public List<OrderResponse> mapToListDto(List<Order> list) {
        List<OrderResponse> responseList = new ArrayList<>();
        for(Order order: list) {
            OrderResponse orderResponse = mapToDto(order);
            responseList.add(orderResponse);
        }
        return responseList;
    }
}
