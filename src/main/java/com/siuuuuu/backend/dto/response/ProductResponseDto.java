package com.siuuuuu.backend.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    @NotNull
    private String name;
    @NotNull
    private String slug;
    @NotNull
    private Integer price;
    @NotNull
    private String description;
    @NotNull
    private String status;
    @NotNull
    private BrandResponseDto brand;
    @NotNull
    private CategoryResponseDto category;
    @NotNull
    private int quantity;
    @NotNull
    private Float rate;
    @NotNull
    private Integer ratedTotal;
}
