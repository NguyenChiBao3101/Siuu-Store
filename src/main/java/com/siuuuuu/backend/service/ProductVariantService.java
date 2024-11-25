package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.ProductVariant;
import com.siuuuuu.backend.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    List<ProductVariant> findAllProductVariantByProductId(String productId) {
        return productVariantRepository.findAllProductVariantByProductId(productId);
    }

    public void createProductVariant(ProductVariant productVariant) {
        productVariantRepository.save(productVariant);
    }

}
