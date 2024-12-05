package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    OrderDetail findOneById(String id);

    @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.productVariant.product.id = :productId")
    long countByProductId(@Param("productId") String productId);

}
