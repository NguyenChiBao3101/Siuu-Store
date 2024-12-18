package com.siuuuuu.backend.specification;

import com.siuuuuu.backend.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterByCategories(List<String> categoryIds){
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Product> filterByBrands(List<String> brandIds){
        return (root, query, criteriaBuilder) -> {
            if (brandIds == null || brandIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("brand").get("id").in(brandIds);
        };
    }

    public static Specification<Product> filterBySizes(List<String> sizes){
        return (root, query, criteriaBuilder) -> {
            if (sizes == null || sizes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("productVariants").join("size").get("name").in(sizes);
        };
    }
}
