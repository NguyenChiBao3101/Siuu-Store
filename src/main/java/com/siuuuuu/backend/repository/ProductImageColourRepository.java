package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.ProductImageColour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductImageColourRepository extends JpaRepository<ProductImageColour, String> {
   List<ProductImageColour> findAllByProductId(String productId);
}
