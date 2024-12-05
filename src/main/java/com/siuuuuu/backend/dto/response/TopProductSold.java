package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopProductSold {
    Product product;
    Long totalSold;
}
