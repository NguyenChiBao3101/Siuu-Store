package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemCartRequest {
    @NotBlank(message = "productVariantId là bắt buộc")
    private String productVariantId;
    @Min(value = 1, message = "chọn số lượng sản phẩm tối thiểu là 1")
    @Max(value = 10, message = "Số lượng tối đa cho mỗi sản phẩm là 10")
    private int quantity;
}
