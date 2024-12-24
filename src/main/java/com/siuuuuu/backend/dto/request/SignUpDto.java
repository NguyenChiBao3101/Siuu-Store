package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
public class SignUpDto {
    @NotEmpty(message = "Họ và tên đệm không được để trống")
    private String first_name;

    @NotEmpty(message = "Tên không được để trống")
    private  String last_name;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date_of_birth;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;

    @NotEmpty(message = "Xác nhận mật khẩu không được để trống")
    private String confirm_password;
}
