package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariant> findAllProductVariantByProductId(String productId);
    List<ProductVariant> findAllByProduct_IdAndProductImageColour_IdOrderBySize_NameAsc(String productId, String productImageColourId);
}
