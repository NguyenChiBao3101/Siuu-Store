package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.Category;
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

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String getAllCategories(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("title", "Danh Mục Sản Phẩm");
        model.addAttribute("categories", categories);
        System.out.println("Categories size: " + categories.size()); // Kiểm tra kích thước danh sách
        return "admin/category/index";
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public String createCategory(@ModelAttribute("category") CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        categoryService.createNewCategory(categoryDto);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/category";
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public String deleteCategory(@PathVariable(name = "id") String id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @RequestMapping(value = {"update/{id}"}, method = RequestMethod.GET)
    public String updateCategory(@PathVariable String id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("title", "Cập Nhật Danh Mục");
        model.addAttribute("category", category);
        return "admin/category/update-category";
    }

    @RequestMapping(value = {"update/{id}"}, method = RequestMethod.POST)
    public String submitUpdateCategory(
            @PathVariable String id,
            @ModelAttribute("category") CategoryDto categoryDto) {

        categoryService.updateCategory(categoryDto, id);
        return "redirect:/admin/category";
    }
}
