package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.service.CategoryService;
import com.siuuuuu.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("products", productService.findAllProduct());
        return "admin/product/index";
    }

    @RequestMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("title", "Thêm Sản Phẩm");
        return "admin/product/create-product";
    }
}
