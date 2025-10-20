package com.siuuuuu.backend.dto.response;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.CartDetail;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    @NotNull
    private AccountDtoResponse accountDtoResponse;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private List<CartDetailResponse> cartDetails;
}
