package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.dto.response.OrderDetailResponse;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    private String note;

    private  List<@NotBlank String> cartDetailIds;

    @NotBlank
    private String paymentMethod;
}
