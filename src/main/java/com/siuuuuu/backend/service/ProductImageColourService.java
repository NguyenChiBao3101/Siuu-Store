package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.repository.ProductImageColourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageColourService {
    @Autowired
    private ProductImageColourRepository productImageColourRepository;

    public ProductImageColour getProductImageColourById(String id) {
        return productImageColourRepository.findById(id).orElse(null);
    }

    public List<ProductImageColour> getProductImageColourByProductId(String productId) {
        return productImageColourRepository.findAllByProductId(productId);
    }

    public ProductImageColour createProductImageColour(ProductImageColour productImageColour) {
        return productImageColourRepository.save(productImageColour);
    }
}
