package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.ProductImageColourRepository;
import com.siuuuuu.backend.repository.ProductVariantRepository;
import com.siuuuuu.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.siuuuuu.backend.entity.*;
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
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ShopController {

    CategoryService categoryService;

    ProductService productService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductImageColourService productImageColourService;
    ProductVariantRepository productVariantRepository;

    OrderDetailService orderDetailService;

    FeedbackService feedbackService;

    @GetMapping("")
    public String shop(Model model, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "12") int size) {
        model.addAttribute("title", "Cửa hàng");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("sizes", sizeService.findAllSize());

        Page<Product> products = productService.getAllProducts(page, size);
        model.addAttribute("products", products);

        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());

        List<ProductImageColour> productImageColours = productImageColourService.findAllProductImageColours();
        Map<String, String> productThumbnails = new HashMap<>();
        for (ProductImageColour productImageColour : productImageColours) {
            String productId = productImageColour.getProduct().getId();
            productThumbnails.put(productId, productImageColourService.getProductThumbnail(productImageColour));
        }
        model.addAttribute("productThumbnails", productThumbnails);
        return "shop/index";
    }

    @GetMapping("/product")
    public String product(@RequestParam("slug") String slug, Model model, @RequestParam("colourId") String colourId) {
        Product product = productService.getProductBySlug(slug);

        try {
            List<ProductImageColour> productImageColours = product.getProductImageColours();
            List<String> imageUrls = new ArrayList<>();
            for (ProductImageColour productImageColour : productImageColours) {
                if (productImageColour.getId().equals(colourId)) {
                    for (ProductImage productImage : productImageColour.getProductImages()) {
                        imageUrls.add(productImage.getImageUrl());
                    }
                }
            }

            List<ProductVariant> productVariants = productVariantRepository.findAllByProduct_IdAndProductImageColour_IdOrderBySize_NameAsc(product.getId(), colourId);
            List<Feedback> feedbacks = feedbackService.getFeedbacksByProduct(product);
            model.addAttribute("feedbacks", feedbacks);
            model.addAttribute("rate", product.getRate());
            model.addAttribute("feedbacksTotal", feedbackService.countfeedbacksByProduct(product.getId()));
            model.addAttribute("sold", orderDetailService.countOrderDetailsByProduct(product.getId()));
            model.addAttribute("title", product.getName());
            model.addAttribute("imageUrls", imageUrls);
            model.addAttribute("product", product);
            model.addAttribute("productVariants", productVariants);
            model.addAttribute("productImageColours", productImageColours);
            model.addAttribute("product0", productImageColours.get(0).getProductImages());
            model.addAttribute("colourSelected", colourId);
        } catch (Error error) {
            throw new RuntimeException();
        }

        return "shop/product-detail";
    }
}
