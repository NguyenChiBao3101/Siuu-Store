package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank(message = "oldPassword không được để trống")
    private String oldPassword;

    @NotBlank(message = "newPassword không được để trống")
    @Size(min = 8, message = "newPassword phải tối thiểu 8 ký tự")
    private String newPassword;
}
