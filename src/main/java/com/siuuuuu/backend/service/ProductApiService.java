package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.request.CreateProductRequest;
import com.siuuuuu.backend.dto.request.ProductDto;
import com.siuuuuu.backend.dto.request.UpdateProductRequest;
import com.siuuuuu.backend.dto.response.ProductResponseDto;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Category;
import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductApiService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return mapToListDto(products);
    }

    public ProductResponseDto getProductBySlug(String slug) {
        Product product = productRepository.findProductBySlug(slug);
        if (product == null) throw new NoSuchElementException("không tìm thấy sản phẩm với slug: " + slug);
        return mapToDto(product);
    }

    public ProductResponseDto createProduct(CreateProductRequest dto) {
        if(!productRepository.existsBySlug(dto.getSlug())) {
            Product p = new Product();
            p.setName(dto.getName());
            p.setSlug(dto.getSlug());
            p.setPrice(dto.getPrice());
            p.setDescription(dto.getDescription());
            p.setCategory(categoryService.getCategoryById(dto.getCategoryId()));
            p.setBrand(brandService.getBrandById(dto.getBrandId()));
            return mapToDto(productRepository.save(p));
        } else {
            throw new IllegalArgumentException("Sản phẩm đã tồn tại với slug");
        }
    }


    public ProductResponseDto updateProduct(String slug, UpdateProductRequest dto) {
         Product product = productRepository.findProductBySlug(slug);
         if(product == null) {
             throw new NoSuchElementException("Không tìm thấy sản phẩm với slug: " + slug);
         }
         product.setName(dto.getName());
         product.setSlug(dto.getSlug());
         product.setDescription(dto.getDescription());
         product.setPrice(dto.getPrice());
         product.setCategory(categoryService.getCategoryById(dto.getCategoryId()));
         product.setBrand(brandService.getBrandById(dto.getBrandId()));
         productRepository.save(product);
         return mapToDto(product);
    }

    public ProductResponseDto mapToDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setSlug(product.getSlug());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setStatus(product.getStatus().toString());
        productResponseDto.setCategory(categoryService.mapDto(product.getCategory()));
        productResponseDto.setBrand(brandService.mapToDto(product.getBrand()));
        productResponseDto.setRate(product.getRate());
        productResponseDto.setRatedTotal(product.getRatedTotal());
        return productResponseDto;
    }

    public List<ProductResponseDto> mapToListDto(List<Product> productList) {
        List<ProductResponseDto> responseDtoList = new ArrayList<>();
        for (Product product: productList) {
            ProductResponseDto productResponseDto = mapToDto(product);
            responseDtoList.add(productResponseDto);
        }
        return responseDtoList;
    }

}

