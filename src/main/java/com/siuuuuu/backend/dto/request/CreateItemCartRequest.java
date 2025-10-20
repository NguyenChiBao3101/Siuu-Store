package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.ProductVariant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemCartRequest {
    @Email
    @NotNull
    private String email;
    @NotNull
    private ProductVariant productVariant;

    @Positive
    @NotNull
    private int quantity;
}
