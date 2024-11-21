package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.Category;
import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.service.CategoryService;
import com.siuuuuu.backend.service.ProductImageColourService;
import com.siuuuuu.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageColourService productImageColourService;
    @GetMapping("")
    public String shop(Model model, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "1") int size) {
        model.addAttribute("title", "Cửa hàng");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Page<Product> products = productService.getAllProducts(page, size);model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());

        List<ProductImageColour> productImageColours = productImageColourService.findAllProductImageColours();
        model.addAttribute("productImageColours", productImageColours);

        Map<String, String> productThumbnails = new HashMap<>();
        for (ProductImageColour productImageColour : productImageColours) {
            try {
                 String productId = productImageColour.getProduct().getId();
                 Product product = productService.findProductById(productId);
                 String thumbnail = productImageColourService.getProductThumbnail(productImageColour);
                 productThumbnails.put(product.getId(), thumbnail);
            } catch (RuntimeException e) {
                 productThumbnails.put(productImageColour.getProduct().getId(), "sh.jpg");
            }
        }
        model.addAttribute("productThumbnails", productThumbnails);
        return "shop/index";
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("title", "Product Variant");
        return "shop/product-variant";
    }
}
