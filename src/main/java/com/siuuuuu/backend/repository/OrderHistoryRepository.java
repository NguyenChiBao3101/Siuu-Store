package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {
    List<OrderHistory> findByOrderIdOrderByCreatedAtAsc(String orderId);
}
