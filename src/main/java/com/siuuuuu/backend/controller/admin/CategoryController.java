package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.Category;
import com.siuuuuu.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String getAllCategories(Model model, @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        Page<CategoryDto> categoryPage = categoryService.getAllCategories(page, size);

        model.addAttribute("title", "Danh Mục Sản Phẩm");
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalItems", categoryPage.getTotalElements());
        model.addAttribute("categories", categoryPage.getContent());
        return "admin/category/index";
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public String createCategory(@ModelAttribute("category") CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        categoryService.createNewCategory(categoryDto);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/category?page=1&size=5";
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
