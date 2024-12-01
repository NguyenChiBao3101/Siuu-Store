package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findProductBySlug(String slug);
}
