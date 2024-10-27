package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.Category;
import com.siuuuuu.backend.repository.CategoryRepository;
import com.siuuuuu.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository repository;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String getAllCategories(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        System.out.println("Categories size: " + categories.size()); // Kiểm tra kích thước danh sách
        return "admin/category";
    }

    @RequestMapping(value= {""}, method = RequestMethod.POST)
    public String createCategory( @ModelAttribute("category") CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        categoryService.createNewCategory(categoryDto);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/category";
    }

    @GetMapping(value = {"/delete/{id}"})
    public String deleteCategory(@PathVariable(name="id") String id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @GetMapping(value = {"update/{id}"})
    public String updateCategory(@PathVariable String id, Model model) {
        Category category =  repository.findById(id).orElseThrow(() -> new RuntimeException("Category not fo"));
        model.addAttribute("category", category);
        return "admin/update-category";
    }

    @PostMapping(value = {"update/{id}"})
    public String submitUpdateCategory(
            @PathVariable String id,
            @ModelAttribute("category") CategoryDto categoryDto,
            RedirectAttributes redirectAttributes) {

        // Fetch the category from the database
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update category properties with data from the form
        category.setName(categoryDto.getName());
        category.setStatus(categoryDto.getStatus());

        // Save the updated category back to the repository
        repository.save(category);

        // Add a flash attribute for a success message
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");

        // Redirect to the category list page
        return "redirect:/admin/category";
    }
}
