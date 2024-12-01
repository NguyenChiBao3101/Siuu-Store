package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByCustomerOrderByCreatedAtDesc(Account account);
}
