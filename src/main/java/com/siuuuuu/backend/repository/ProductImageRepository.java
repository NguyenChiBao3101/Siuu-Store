package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.ProductImage;
import com.siuuuuu.backend.entity.ProductImageColour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
}
