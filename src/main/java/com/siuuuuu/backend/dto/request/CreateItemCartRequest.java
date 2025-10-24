package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.dto.response.ProductVariantResponse;

import com.siuuuuu.backend.entity.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemCartRequest {
    @NotBlank(message = "productVariantId là bắt buộc")
    private String productVariantId;
    @Positive
    @NotNull
    private int quantity;
}
