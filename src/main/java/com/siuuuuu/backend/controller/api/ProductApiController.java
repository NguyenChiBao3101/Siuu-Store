package com.siuuuuu.backend.controller.api;
import com.siuuuuu.backend.dto.request.CreateProductRequest;
import com.siuuuuu.backend.dto.request.UpdateProductRequest;
import com.siuuuuu.backend.service.ProductApiService;
import jakarta.validation.Valid;

import com.siuuuuu.backend.dto.response.ProductResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductApiController {
    @Autowired
    private ProductApiService productApiService;
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> listAll() {
        List<ProductResponseDto> products = productApiService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductResponseDto> getBySlug(@PathVariable String slug) {
        ProductResponseDto product = productApiService.getProductBySlug(slug);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        ProductResponseDto created= productApiService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String slug,
                                                  @Valid @RequestBody UpdateProductRequest request) {
        ProductResponseDto updated = productApiService.updateProduct(slug, request);
        return ResponseEntity.ok(updated);
    }
}
