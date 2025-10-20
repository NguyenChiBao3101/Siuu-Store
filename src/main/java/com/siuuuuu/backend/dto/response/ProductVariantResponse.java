package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.entity.Size;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {
    @NotNull
    private String sku;
//    @Positive
//    @NotNull
//    private int quantity;
    @CreatedDate
    private LocalDateTime createdAt;
}
