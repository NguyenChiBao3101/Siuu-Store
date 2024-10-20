package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;
    private String statusString;  // Nhận từ form ("Đang hoạt động" / "Ngừng hoạt động")
}
