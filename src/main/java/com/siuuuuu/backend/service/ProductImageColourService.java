package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.entity.ProductImage;
import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.repository.ProductImageColourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImageColourService {
    @Autowired
    private ProductImageColourRepository productImageColourRepository;

    public List<ProductImageColour> findAllProductImageColours() {
        return productImageColourRepository.findAll();
    }

    public ProductImageColour getProductImageColourById(String id) {
        return productImageColourRepository.findById(id).orElse(null);
    }

    public List<ProductImageColour> getProductImageColourByProductId(String productId) {
        return productImageColourRepository.findAllByProductId(productId);
    }

    public ProductImageColour createProductImageColour(ProductImageColour productImageColour) {
        return productImageColourRepository.save(productImageColour);
    }

    public String getProductThumbnail(ProductImageColour productImageColour) {
        List<ProductImage> images = null;
        if (productImageColour != null) {
            images = productImageColour.getProductImages();
        }
        if (images == null || images.isEmpty()) {
            return "assets/image/shop/sh.jpg";
        }
        return images.get(0).getImageUrl();
    }

}
