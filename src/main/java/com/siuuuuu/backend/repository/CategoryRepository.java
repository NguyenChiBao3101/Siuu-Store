package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Category;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Override
    Page<Category> findAll(Pageable pageable);

    boolean existsCategoriesByName(String name);

}
