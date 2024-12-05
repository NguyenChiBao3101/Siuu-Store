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
public class RegisterDto {
    @NotBlank(message = "Họ không được để trống.")
    private String firstName;

    @NotBlank(message = "Tên không được để trống.")
    private String lastName;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ.")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+84|0)[1-9][0-9]{8}$", message = "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng.")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ.")
    @NotBlank(message = "Email không được để trống.")
    private String email;

    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
    @NotBlank(message = "Mật khẩu không được để trống.")
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống.")
    private String confirmPassword;

    private String roleName;

    private Boolean isActive;
}
