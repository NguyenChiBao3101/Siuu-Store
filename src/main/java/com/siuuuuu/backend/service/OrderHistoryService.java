package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.dto.response.OrderDetailResponse;
import com.siuuuuu.backend.dto.response.OrderHistoryResponse;
import com.siuuuuu.backend.dto.response.OrderResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.repository.OrderHistoryRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public OrderHistoryResponse mapToDto(OrderHistory orderHistory) {
        if (orderHistory == null)  {
            throw new NoSuchElementException("Lịch sử đơn đặt hàng không tồn tại");
        }
        OrderHistoryResponse response = OrderHistoryResponse.builder()
                .orderId(orderHistory.getId())
                .status(orderHistory.getStatus())
                .note(orderHistory.getOrder().getShippingNote())
                .createdAt(orderHistory.getCreatedAt().toString())
                .build();
        return response;
    }
    public List<OrderHistoryResponse> mapToListDto(List<OrderHistory> list) {
        List<OrderHistoryResponse> responseList = new ArrayList<>();
        for(OrderHistory order: list) {
            OrderHistoryResponse response = mapToDto(order);
            responseList.add(response);
        }
        return responseList;
    }
}
