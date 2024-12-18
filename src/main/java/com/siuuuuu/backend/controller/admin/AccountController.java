package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.dto.request.AccountDto;
import com.siuuuuu.backend.dto.request.RegisterDto;
import com.siuuuuu.backend.dto.request.UpdateProfileDto;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import com.siuuuuu.backend.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/account")
@PreAuthorize("hasAuthority('ADMIN')")
public class AccountController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping("")
    public String accounts(Model model,
                           @RequestParam(defaultValue = "1") Boolean isActive,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(value = "status", required = false) String status
    ) {
        Page<AccountDto> accountDtoPage;
        if (status != null && !status.isEmpty()) {
            accountDtoPage = accountService.getEmployeeByStatus(page, size, Boolean.parseBoolean(status));
        } else {
            accountDtoPage = accountService.getEmployeeAccounts(page, size);
        }

        model.addAttribute("title", "Tài khoản");
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", accountDtoPage.getTotalPages());
        model.addAttribute("totalItems", accountDtoPage.getTotalElements());
        model.addAttribute("accounts", accountDtoPage.getContent());
        model.addAttribute("status", status);
        return "admin/account/index";
    }

    // Hiển thị form đăng ký tài khoản
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        Set<Role> allRoles = new LinkedHashSet<>();
        for (Role role : roleRepository.findAllRolesAsSet()) {
            if (!role.getName().equals(Roles.ADMIN.name())) {
                allRoles.add(role);
            }
        }

        RegisterDto register = new RegisterDto();
        register.setRoleName(allRoles.iterator().next().getName());

        model.addAttribute("allRoles", allRoles);
        model.addAttribute("register", register);
        return "admin/account/register";
    }

    @PostMapping("/register")
    public String registerAccount(@Valid @ModelAttribute("register") RegisterDto registerDto,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {
        Set<Role> allRoles = new HashSet<>();
        for (Role role : roleRepository.findAllRolesAsSet()) {
            if (!role.getName().equals(Roles.ADMIN.name())) {
                allRoles.add(role);
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập vào.");
            return "admin/account/register";
        }
        try {
            accountService.register(registerDto);
            redirectAttributes.addFlashAttribute("message", "Thêm tài khoản thành công!");
            return "redirect:/admin/account";
        } catch (RuntimeException ex) {
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("errorMessage", ex.getMessage());
            return "admin/account/register";
        }
    }

    @GetMapping("/update/{email}")
    public String getUpdateForm(@PathVariable String email, Model model) {
        RegisterDto registerDto = accountService.showUpdateForm(email);
        System.out.print("get dto " + registerDto.toString());
        Set<Role> allRoles = new LinkedHashSet<>();
        for (Role role : roleRepository.findAllRolesAsSet()) {
            if (!role.getName().equals(Roles.ADMIN.name())) {
                allRoles.add(role);
            }
        }
        registerDto.setRoleName(allRoles.iterator().next().getName());

        model.addAttribute("registerDto", registerDto);
        model.addAttribute("accountEmail", email);
        model.addAttribute("allRoles", allRoles);
        return "admin/account/update-account";
    }

    @PostMapping("/update/{email}")
    public String updateAccount(@PathVariable String email,
                                @Valid UpdateProfileDto updateProfileDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Set<Role> allRoles = new HashSet<>();
        for (Role role : roleRepository.findAllRolesAsSet()) {
            if (!role.getName().equals(Roles.ADMIN.name())) {
                allRoles.add(role);
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập vào.");
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("accountEmail", email);
            model.addAttribute("registerDto", updateProfileDto);
            System.out.println(updateProfileDto.toString());
            return "admin/account/update-account";
        }
        try {
            accountService.updateAccount(updateProfileDto, email);
            redirectAttributes.addFlashAttribute("message", "Cập nhật tài khoản thành công!");
            return "redirect:/admin/account";
        } catch (RuntimeException ex) {
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("registerDto", updateProfileDto);
            model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi không xác định.");
            return "admin/account/update-account";
        }
    }

}
