package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.ProductImageColourRepository;
import com.siuuuuu.backend.repository.ProductVariantRepository;
import com.siuuuuu.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @Autowired
    private SizeService sizeService;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductImageColourRepository productImageColourRepository;

    @GetMapping("")
    public String shop(Model model, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "1") int size) {
        model.addAttribute("title", "Cửa hàng");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Page<Product> products = productService.getAllProducts(page, size);
        model.addAttribute("products", products);
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
    public String product(@RequestParam("slug") String slug, Model model, @RequestParam("colourId") String colourId ) {
        Product product = productService.getProductBySlug(slug);
        List<ProductImageColour> productImageColours = product.getProductImageColours();
        List<Size> sizes = sizeService.findAllSize();
        List<String>imageUrls = new ArrayList<>();
        for(ProductImageColour productImageColour : productImageColours) {
            if(productImageColour.getId().equals(colourId)) {
                for(ProductImage productImage : productImageColour.getProductImages()) {
                    imageUrls.add(productImage.getImageUrl());
                }
            }
        }
        System.out.println(slug+"---"+colourId);
        List<ProductVariant> productVariants = productVariantRepository.findAllByProduct_IdAndProductImageColour_IdOrderBySize_NameAsc(product.getId(), colourId);
        System.out.println(productVariants);

        model.addAttribute("imageUrls", imageUrls);
        model.addAttribute("product", product);
        model.addAttribute("sizes", sizes);
        model.addAttribute("productVariants", productVariants);
        model.addAttribute("productImageColours", productImageColours);
        model.addAttribute("product0", productImageColours.get(0).getProductImages());
        model.addAttribute("colourSelected", colourId);
        return "shop/product-detail";
    }


}
