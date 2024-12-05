package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.request.CategoryDto;
import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.ProductImageRepository;
import com.siuuuuu.backend.repository.ProductVariantRepository;
import com.siuuuuu.backend.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.siuuuuu.backend.repository.ProductRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductImageColourService productImageColourService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductVariantService productVariantService;


    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    //Get all products with pagination
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return productRepository.searchByName(name, pageable);
    }

    public Page<Product> getFilteredProducts(int page, int size, List<String> categoryIds, List<String> brandIds) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (categoryIds != null && !categoryIds.isEmpty()) {
            return productRepository.findByCategoryIdIn(pageable, categoryIds);
        }
        if (brandIds != null && !brandIds.isEmpty()) {
            return productRepository.findByBrandIdIn(pageable, brandIds);
        }
        return productRepository.findAll(pageable);
    }
    public Product findProductById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findProductBySlug(slug);
    }

    public List<ProductVariant> getProductVariantByProductId(String productId) {
        return productVariantService.findAllProductVariantByProductId(productId);
    }

    public void createProductVariant(String slug, List<String> sizes, String productImageColour) {
        Product product = getProductBySlug(slug);
        sizes.forEach(sizeId -> {
            Size size = sizeRepository.findById(sizeId).orElse(null);
            if (Objects.isNull(size)) {
                return;
            }
            ProductVariant productVariant = new ProductVariant();
            productVariant.setProduct(product);
            productVariant.setSize(size);
            productVariant.setProductImageColour(productImageColourService.getProductImageColourById(productImageColour));
            productVariant.setSku(generateProductVariantSKU(product, size, productVariant.getProductImageColour().getId()));
            productVariantService.createProductVariant(productVariant);
        });
    }

    public String generateProductVariantSKU(Product product, Size size, String colour) {
        return product.getSlug() + "-" + size.getName() + "-" + colour.substring(0, 8);
    }

    public void createProductImageColour(String slug, MultipartFile[] images) {
        Product product = getProductBySlug(slug);
        ProductImageColour productImageColour = productImageColourService.createProductImageColour(new ProductImageColour(product));
        List<String> imageUrls = null;
        try {
            imageUrls = cloudinaryService.uploadFiles(images).stream().map(map -> map.get("url").toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(imageUrls)) {
            imageUrls.forEach(imageUrl -> productImageService.createProductImage(productImageColour.getId(), imageUrl));
        }
    }

    public String getProductThumbnail(ProductImageColour productImageColour) {
        List<ProductImage> images = new ArrayList<>();
        if (productImageColour != null) {
            images = productImageColour.getProductImages();
        }
        if (images == null || images.isEmpty()) {
            return "assets/image/shop/sh.jpg";
        }
        return images.get(0).getImageUrl();
    }

    public void solvingRating(Feedback feedback) {
        Product product = feedback.getProduct();
        int preRateTotal = product.getRatedTotal();
        product.setRatedTotal(preRateTotal+1);
        float rate;
        if(Objects.nonNull(product.getRate()) && product.getRate()>0) {
            float preRate = product.getRate();
            rate = (preRate*preRateTotal + feedback.getRate()) / ((float) product.getRatedTotal());
        }else{
            rate = (float) feedback.getRate();
        }

        product.setRate(rate);

        productRepository.save(product);
    }
}
