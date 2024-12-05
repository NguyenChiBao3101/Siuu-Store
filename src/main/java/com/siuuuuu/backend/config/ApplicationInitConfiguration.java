package com.siuuuuu.backend.config;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.siuuuuu.backend.constant.Roles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class ApplicationInitConfiguration implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProfileRepository profileRepository;

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
            admin.setIsActive(true);
            admin.getRoles().add(adminRole);
            Account account = accountRepository.save(admin);
            Profile profile = new Profile();
            profile.setAccount(account);
            profile.setAvatarUrl("@{/assets/image/tungchinh.jpg}");
            profile.setDateOfBirth(LocalDate.now());
            profile.setFirstName("Web");
            profile.setLastName("Admin");
            profile.setPhoneNumber("0123456789");
            profile.setIsActive(true);
            profileRepository.save(profile);
        } else if(profileRepository.findByAccount_Email("admin@gmail.com")==null) {
            Account account =  accountRepository.findByEmail("admin@gmail.com");
            Profile profile = new Profile();
            profile.setAccount(account);
            profile.setAvatarUrl("@{/assets/image/tungchinh.jpg}");
            profile.setDateOfBirth(LocalDate.now());
            profile.setFirstName("Web");
            profile.setLastName("Admin");
            profile.setPhoneNumber("0123456789");
            profile.setIsActive(true);
            profileRepository.save(profile);
        }

        if (!accountRepository.existsByEmail("customer@gmail.com")) {
            Role customerRole = roleRepository.findByName(Roles.CUSTOMER.name());
            Account customer = new Account();
            customer.setEmail("customer@gmail.com");
            customer.setPassword(passwordEncoder.encode("customer"));
            customer.setIsActive(true);
            customer.getRoles().add(customerRole);
            Account account = accountRepository.save(customer);
            Profile profile = new Profile();
            profile.setAccount(account);
            profile.setAvatarUrl("@{/assets/image/tungchinh.jpg}");
            profile.setDateOfBirth(LocalDate.now());
            profile.setFirstName("Web");
            profile.setLastName("Customer");
            profile.setPhoneNumber("0123456789");
            profile.setIsActive(true);
            profileRepository.save(profile);
        }else if(profileRepository.findByAccount_Email("customer@gmail.com")==null){
            Account account = accountRepository.findByEmail("customer@gmail.com");
            Profile profile = new Profile();
            profile.setAccount(account);
            profile.setAvatarUrl("@{/assets/image/tungchinh.jpg}");
            profile.setDateOfBirth(LocalDate.now());
            profile.setFirstName("Web");
            profile.setLastName("Customer");
            profile.setPhoneNumber("0123456789");
            profile.setIsActive(true);
            profileRepository.save(profile);
        }



        // Create cart using account id if it not exits
        accountRepository.findAll().forEach(account -> {
            if (cartRepository.findByAccount(account) == null) {
                if (account.getCart() == null) {
                    Cart newCart = new Cart();
                    newCart.setAccount(account);
                    account.setCart(newCart);
                }
                cartRepository.save(account.getCart());
            }
        });
    }
}
