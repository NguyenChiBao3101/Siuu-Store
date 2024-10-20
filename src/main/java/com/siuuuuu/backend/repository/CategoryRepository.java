package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsCategoriesByName(String name);
}
