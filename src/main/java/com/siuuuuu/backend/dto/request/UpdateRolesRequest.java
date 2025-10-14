package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter; @Getter
public class UpdateRolesRequest {
    @NotEmpty
    private java.util.Set<String> roles;
}