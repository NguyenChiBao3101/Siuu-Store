package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Feedback;
import com.siuuuuu.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findAllByProductOrderByCreatedAtDesc(Product product);

    @Query("SELECT COUNT(fb) FROM Feedback fb WHERE fb.product.id = :productId")
    long countByProductId(@Param("productId") String productId);
}
