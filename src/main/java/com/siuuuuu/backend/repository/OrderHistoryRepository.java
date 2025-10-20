package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {
    List<OrderHistory> findByOrderIdOrderByCreatedAtAsc(String orderId);
    
    @Query("SELECT oh FROM OrderHistory oh " +
           "JOIN oh.order o " +
           "JOIN o.customer c " +
           "WHERE c.email = :email " +
           "ORDER BY oh.createdAt ASC")
    List<OrderHistory> findByCustomerEmailOrderByCreatedAtAsc(@Param("email") String email);
}
