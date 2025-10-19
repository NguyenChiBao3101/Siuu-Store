package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    @NotBlank String name;
    @NotBlank String slug;
    @NotNull
    @Min(1)
    Integer price;
    @NotBlank String description;
    @NotBlank String categoryId;
    @NotBlank String brandId;
    @NotBlank int quantity;
}
