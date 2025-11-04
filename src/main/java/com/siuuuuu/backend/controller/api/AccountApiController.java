package com.siuuuuu.backend.controller.api;

import com.siuuuuu.backend.dto.request.*;
import com.siuuuuu.backend.dto.response.AccountDtoResponse;
import com.siuuuuu.backend.dto.response.ProfileDtoResponse;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountApiController {
    private final AccountService accountService;

    @Autowired
    public AccountApiController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<AccountDtoResponse>> list() {
        List<AccountDtoResponse> accounts = accountService.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{email}")
    //@PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE') or #email == authentication.name")
    public ResponseEntity<AccountDtoResponse> getByEmail(@PathVariable String email) {
        Account account = accountService.findByEmail(email);
        return ResponseEntity.ok(accountService.mapToDto(account));
    }


    @PutMapping("/{email}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountDtoResponse> updateAccountStatus(
            @PathVariable String email,
            @Valid @RequestBody UpdateAccountStatusDto updateAccountStatusDto
    ) {
        AccountDtoResponse updated = accountService.updateAccountAPI(updateAccountStatusDto, email);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{email}/profile")
    public ResponseEntity<ProfileDtoResponse> getProfile(@PathVariable String email) {
        ProfileDtoResponse profile = accountService.getProfileAPI(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{email}/profile")
//    @PreAuthorize("#email == authentication.name")
    public ResponseEntity<ProfileDtoResponse> updateProfile(
            @PathVariable String email,
            @Valid @RequestBody UpdateProfileDto updateProfileDto
    ) {
        ProfileDtoResponse profileDtoResponse = accountService.updateProfileAPI(updateProfileDto, email);
        return ResponseEntity.ok(profileDtoResponse);
    }

    @PutMapping("/{email}/password")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> changePassword(
            @PathVariable String email,
            @Valid @RequestBody UpdatePasswordRequest passwordRequest
            ) {
        accountService.changePassword(email, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/roles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<AccountDtoResponse> updateRoles(
            @RequestBody UpdateRolesRequest roles
            ) {
        AccountDtoResponse updatedAccount = accountService.updateRoles(roles);
        return ResponseEntity.ok(updatedAccount);
    }

}
