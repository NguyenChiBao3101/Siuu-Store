package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.entity.ProductVariant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponse {
    @Id
    private String id;
    @NotNull
    private ProductVariantResponse productVariantResponse;
    @Positive
    @NotNull
    private int quantity;
}
