package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.repository.OrderHistoryRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderHistoryService {
    OrderRepository orderRepository;

    OrderHistoryRepository orderHistoryRepository;

    // Get order history by order id and sort by created date
    public List<OrderHistory> getOrderHistoriesByOrderId(String orderId) {
        return orderHistoryRepository.findByOrderIdOrderByCreatedAtAsc(orderId);
    }

    public void addOrderHistory(String orderId, OrderStatus status, Account actionBy) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setStatus(status);
            orderHistory.setActionBy(actionBy);
            orderHistoryRepository.save(orderHistory);
        }
    }
}
