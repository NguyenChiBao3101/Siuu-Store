package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, String> {

}
