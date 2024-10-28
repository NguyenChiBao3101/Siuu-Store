package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siuuuuu.backend.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

}
