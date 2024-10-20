package com.siuuuuu.backend.controller;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

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


    @RequestMapping(value= {"/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Danh mục đã được xóa thành công");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Danh mục không tồn tại");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi xóa danh mục");
        }
    }




//    @GetMapping("/all")
//    public ResponseEntity<List<CategoryDto>> getAllCategories() {
//        return ResponseEntity.ok(categoryService.getAllCategories());
//    }
//    @GetMapping("/by_id/{id}")
//    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String id){
//        return ResponseEntity.ok(categoryService.getCategoryById(id));
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<CategoryDto> createCategory( @RequestBody CategoryDto categoryDto) {
//        return ResponseEntity.ok(categoryService.createNewCategory(categoryDto));
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String id) {
//        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,id));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable("id") String id) {
//        boolean deleted = categoryService.deleteCategory(id);
//        if (deleted) {
//            return ResponseEntity.ok("Category deleted successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
//        }
//    }

}
