package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findProductBySlug(String slug);
}
