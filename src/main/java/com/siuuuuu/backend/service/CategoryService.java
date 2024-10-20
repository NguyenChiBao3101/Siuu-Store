package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.Category;
import com.siuuuuu.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
     private CategoryRepository categoryRepository;

    //get all categories
    public List<CategoryDto> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return mapToListDto(categoryList);
    }

    //get all category by name
    public CategoryDto getCategoryById(String id) {
        Category categoryOptional = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy mã danh mục sản phẩm"));
        return mapToDto(categoryOptional);
    }

    //insert new category
    public CategoryDto createNewCategory(CategoryDto categoryDto) {
        Category category = new Category();

        if (categoryRepository.existsCategoriesByName(categoryDto.getName())) {
            throw new RuntimeException("Danh mục sản phẩm này đã tồn tại");
        }

        category.setName(categoryDto.getName());
        category.setCreatedAt(categoryDto.getCreatedAt());
        category.setUpdatedAt(categoryDto.getUpdatedAt());
        category.setStatus(categoryDto.getStatusString().equalsIgnoreCase("active")); // true/false
        System.out.println("Status String: " + categoryDto.getStatusString());
        return mapToDto(categoryRepository.save(category));
    }


    //delete category by id
    public boolean deleteCategory(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }


    //update category information
    public CategoryDto updateCategory(CategoryDto categoryDto ,String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("không tìm thấy danh mục sản phẩm"));
        if(category.getName() != null) {
            category.setName(categoryDto.getName());
        }
        if(category.getCreatedAt() != null) {
            category.setCreatedAt(categoryDto.getCreatedAt());
        }
        if(category.getUpdatedAt() != null) {
            category.setUpdatedAt(categoryDto.getUpdatedAt());
        }
        if(category.getStatus() != null) {
            category.setStatus(categoryDto.getStatus());
        }
        return mapToDto(categoryRepository.save(category));
    }

    public CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setStatus(category.getStatus());
        categoryDto.setCreatedAt(category.getCreatedAt());
        categoryDto.setUpdatedAt(category.getUpdatedAt());

        return categoryDto;
    }

    public List<CategoryDto> mapToListDto(List<Category> categoryList) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for(Category category : categoryList) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());
            categoryDto.setStatus(category.getStatus());
            categoryDto.setCreatedAt(category.getCreatedAt());
            categoryDto.setUpdatedAt(category.getUpdatedAt());

            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }
}
