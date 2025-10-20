package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.constant.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderHistoryResponse {
    @NotNull
    private final String orderId;
    @NotNull
    private final OrderStatus status;
    @NotNull
    private final String note;
    @NotNull
    private final String createdAt;
    @NotNull
    private final AccountDtoResponse accountDtoResponse;
}
