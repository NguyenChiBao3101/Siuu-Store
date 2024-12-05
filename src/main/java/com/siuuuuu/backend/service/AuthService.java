package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.controller.AuthController;
import com.siuuuuu.backend.dto.request.SignUpDto;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;
    @Transactional
    public String signUp(SignUpDto signUpDto) {



            // Tạo tài khoản mới
            Account account = new Account();
            account.setEmail(signUpDto.getEmail());
            account.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

            // Gán role mặc định là CUSTOMER
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName(Roles.CUSTOMER.name());
            if (role != null) {
                roles.add(role);
            } else {
                logger.error("Role CUSTOMER không tồn tại trong database!");
                throw new RuntimeException("Role CUSTOMER không tồn tại");
            }
            account.setRoles(roles);

            Account accountCreated = accountRepository.save(account);
            logger.info("Tài khoản được tạo thành công: {}", accountCreated);

            // Tạo profile mới liên kết với tài khoản
            Profile profile = new Profile();
            profile.setAccount(accountCreated);
            profile.setFirstName(signUpDto.getFirst_name());
            profile.setLastName(signUpDto.getLast_name());
            profile.setDateOfBirth(signUpDto.getDate_of_birth());
            profile.setIsActive(true);

            Profile profileCreated = profileRepository.save(profile);
            logger.info("Profile được tạo thành công: {}", profileCreated);

            // Tạo cart mới liên kết với tài khoản
            Cart cart = new Cart();
            cart.setAccount(accountCreated);
            Cart cartCreated = cartRepository.save(cart);
            logger.info("Cart được tạo thành công: {}", cartCreated);

            return "Success";


    }
}
