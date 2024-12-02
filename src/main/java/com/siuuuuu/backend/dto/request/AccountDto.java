package com.siuuuuu.backend.dto.request;

import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Set<Role> roles;

    @NotNull
    private Profile profile;
}
