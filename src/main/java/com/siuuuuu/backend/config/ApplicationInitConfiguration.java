package com.siuuuuu.backend.config;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.siuuuuu.backend.constant.Roles;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApplicationInitConfiguration implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method that runs automatically after the application starts
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (Roles role : Roles.values()) {
            if (!roleRepository.existsByName(role.name())) {
                roleRepository.save(new Role(role.name()));
            }
        }

        if (!accountRepository.existsByEmail("admin@gmail.com")) {
            Role adminRole = roleRepository.findByName(Roles.ADMIN.name());
            Account admin = new Account();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.getRoles().add(adminRole);
            accountRepository.save(admin);
        }

        if (!accountRepository.existsByEmail("customer@gmail.com")) {
            Role customerRole = roleRepository.findByName(Roles.CUSTOMER.name());
            Account customer = new Account();
            customer.setEmail("customer@gmail.com");
            customer.setPassword(passwordEncoder.encode("customer"));
            customer.getRoles().add(customerRole);
            accountRepository.save(customer);
        }
    }
}
