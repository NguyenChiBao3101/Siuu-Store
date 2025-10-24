package com.siuuuuu.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.Set;

@Getter
public class UpdateRolesRequest {
    private String email;
    @NotEmpty
    private Set<String> roles;
}