package com.siuuuuu.backend.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDtoResponse {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Boolean isVerified;
    @NotNull
    private Set<String> roles;

    @NotNull
    private ProfileDtoResponse profile;
}
