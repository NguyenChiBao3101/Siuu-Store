package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuantityRequest {
    @NotNull
    private String cartDetailId;

    @NotNull
    @Min(value = 1, message = "chọn số lượng sản phẩm tối thiểu là 1")
    @Max(value = 10, message = "Số lượng tối đa cho mỗi sản phẩm là 10")
    private int quantity;
}
