package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findProductBySlug(String slug);

    Page<Product> findByCategoryIdIn(Pageable pageable, List<String> categoryIds);

    Page<Product> findByBrandIdIn(Pageable pageable, List<String> brandIds);
}
