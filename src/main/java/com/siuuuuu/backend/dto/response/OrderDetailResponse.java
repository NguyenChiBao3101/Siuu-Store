package com.siuuuuu.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private String productName;
    private int quantity;
    private int totalPrice;
}
