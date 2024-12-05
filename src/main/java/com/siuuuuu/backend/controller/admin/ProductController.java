package com.siuuuuu.backend.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ProductImageColourService productImageColourService;

    @RequestMapping("")
    public String index(Model model, @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.getAllProducts(page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("title", "Quản Lý Sản Phẩm");
        model.addAttribute("products", products);
        return "admin/product/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateProduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("title", "Thêm Sản Phẩm");
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "admin/product/create-product";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postCreateProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/{slug}", method = RequestMethod.GET)
    public String getProductDetail(@PathVariable String slug, Model model) {
        Product product = productService.getProductBySlug(slug);
        List<Category> categories = categoryService.getAllCategories();
        List<Brand> brands = brandService.getAllBrands();
        List<ProductVariant> productVariants = productService.getProductVariantByProductId(product.getId());
        model.addAttribute("title", "Chi Tiết Sản Phẩm");
        model.addAttribute("productVariants", productVariants);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "admin/product/product-detail";
    }

    @RequestMapping(value = "/{slug}/create/variant", method = RequestMethod.GET)
    public String getCreateProductVariant(@PathVariable String slug, Model model) {
        List<Size> sizes = sizeService.findAllSize();
        Product product = productService.getProductBySlug(slug);
        List<ProductImageColour> productImageColours = productImageColourService.getProductImageColourByProductId(product.getId());
        model.addAttribute("title", "Thêm Chi Tiết Sản Phẩm");
        model.addAttribute("productImageColour", productImageColours);
        model.addAttribute("sizes", sizes);
        return "admin/product/create-product-variant";
    }

    @RequestMapping(value = "/{slug}/create/variant", method = RequestMethod.POST)
    public String postCreateProductVariant(@PathVariable String slug, @RequestParam("productImageColour") String productImageColour, @RequestParam("sizes") List<String> sizes, Model model) {
        productService.createProductVariant(slug, sizes, productImageColour);
        return "redirect:/admin/product/{slug}";
    }

    @RequestMapping(value = "/{slug}/create/product-image-colour", method = RequestMethod.POST)
    public String createProductImageColour(@PathVariable String slug, @RequestParam("images") MultipartFile[] images, Model model) {
        productService.createProductImageColour(slug, images);
        return "redirect:/admin/product/{slug}/create/variant";
    }

}
