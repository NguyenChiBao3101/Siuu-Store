package com.siuuuuu.backend.service;

import com.siuuuuu.backend.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siuuuuu.backend.entity.ProductImage;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageColourService productImageColourService;

    public void createProductImage(String productImageColour, String imageUrl) {
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(imageUrl);
        productImage.setProductImageColour(productImageColourService.getProductImageColourById(productImageColour));
        productImageRepository.save(productImage);
    }
}
