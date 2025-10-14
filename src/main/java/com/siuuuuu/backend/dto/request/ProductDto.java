package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.entity.Brand;
import com.siuuuuu.backend.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDto {

    @NotBlank(message = "Tên sản phẩm không được để trống.")
    private String name;
    @NotBlank(message = "Tên định danh sản phẩm không được để trống.")
    private String slug;

    @NotNull(message = "Giá sản phẩm không được để trống.")
    @Min(value = 1, message = "Giá sản phẩm phải lớn hơn 0.")
    private Integer price;

    @NotBlank(message = "Mô tả sản phẩm không được để trống.")
    private String description;

    @NotNull(message = "Danh mục không được để trống.")
    private Category category;

    @NotNull(message = "Thương hiệu không được để trống.")
    private Brand brand;
}
