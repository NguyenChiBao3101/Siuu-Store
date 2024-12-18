package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {
    @NotBlank(message = "Họ không được để trống.")
    private String firstName;

    @NotBlank(message = "Tên không được để trống.")
    private String lastName;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ.")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+84|0)[1-9][0-9]{8}$", message = "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng.")
    private String phoneNumber;

    private Boolean isActive;
}
