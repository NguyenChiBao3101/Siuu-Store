package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.dto.request.AccountDto;
import com.siuuuuu.backend.dto.request.RegisterDto;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.entity.Role;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.CartRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import com.siuuuuu.backend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AccountDto> getAllAccounts(Boolean isActive) {
        return mapToListDto(accountRepository.findByIsActive(isActive));
    }

    // Get all accounts with Pagination and role is EMPLOYEE
    public Page<AccountDto> getAllAccountsWithPagination(int page, int size, Boolean isActive) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Account> accountDtoPage = accountRepository.findAll(pageable);
        return accountDtoPage.map(this::mapToDto);
    }
    @Transactional
    public AccountDto register(RegisterDto registerDto) {
        // Check input
        if (accountRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Tài khoản email đã tồn tại");
        }
        Account newAccount = new Account();
        newAccount.setEmail(registerDto.getEmail());
        newAccount.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(registerDto.getRoleName()));

        newAccount.setRoles(roles);
        newAccount.setIsActive(registerDto.getIsActive());

        Account accountCreated = accountRepository.save(newAccount);

        Cart cart = new Cart();
        cart.setAccount(accountCreated);
        cartRepository.save(cart);
        // Profile
        Profile profile = new Profile();
        profile.setFirstName(registerDto.getFirstName());
        profile.setLastName(registerDto.getLastName());
        profile.setPhoneNumber(registerDto.getPhoneNumber());
        profile.setDateOfBirth(registerDto.getDateOfBirth());
        profile.setIsActive(true);
        profile.setAccount(accountCreated);

        profileRepository.save(profile);

        System.out.print(accountCreated.toString());

        return mapToDto(accountCreated);
    }



    public RegisterDto showUpdateForm(String email) {
        Account account = accountRepository.findByEmail(email);
        Profile profile = profileRepository.findByAccount_Email(account.getEmail());
        if (profile == null) {
            throw new RuntimeException("Không tìm thấy Profile liên kết với tài khoản email: " + email);
        }

        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail(account.getEmail());
        registerDto.setPassword(account.getPassword());
        registerDto.setRoleName(account.getRoles().iterator().toString()); // Lấy role đầu tiên (nếu có nhiều role)
        registerDto.setIsActive(account.getIsActive());
        registerDto.setFirstName(profile.getFirstName());
        registerDto.setLastName(profile.getLastName());
        registerDto.setDateOfBirth(profile.getDateOfBirth());
        registerDto.setPhoneNumber(profile.getPhoneNumber());

        return registerDto;
    }

    public AccountDto updateAccount(RegisterDto registerDto, String email) {

        // Update account's information
        Account currentAccount = accountRepository.findByEmail(email);
        if(currentAccount.getPassword() != null) {
            currentAccount.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        }
        if(currentAccount.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(registerDto.getRoleName()));
            currentAccount.setRoles(roles);
        }
        if(currentAccount.getIsActive() != null) {
            currentAccount.setIsActive(registerDto.getIsActive());
        }
         Account newAccount = accountRepository.save(currentAccount);

        // update currentProfile's information
        Profile currentProfile = profileRepository.findByAccount_Email(email);
        if(currentProfile.getFirstName() != null) {
            currentProfile.setFirstName(registerDto.getFirstName());
        }
        if(currentProfile.getLastName() != null) {
            currentProfile.setLastName(registerDto.getLastName());
        }
        if(currentProfile.getDateOfBirth() != null) {
            currentProfile.setDateOfBirth(registerDto.getDateOfBirth());
        }
        if(currentProfile.getPhoneNumber() != null) {
            currentProfile.setPhoneNumber(registerDto.getPhoneNumber());
        }
        if(currentProfile.getIsActive() != null) {
            currentProfile.setIsActive(registerDto.getIsActive());
        }
        if(currentProfile.getAccount() != null) {
            currentProfile.setAccount(newAccount);
        }
        profileRepository.save(currentProfile);

        return mapToDto(newAccount);
    }

    public boolean deleteCategory(String id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public AccountDto mapToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(account.getEmail());
        accountDto.setIsActive(account.getIsActive());
        accountDto.setRoles(account.getRoles());
        accountDto.setProfile(account.getProfile());
        return accountDto;
    }


    public List<AccountDto> mapToListDto(List<Account> accountList) {
        List<AccountDto> accountDtoList = new ArrayList<>();
        for (Account account : accountList) {
            accountDtoList.add(mapToDto(account));
        }
        return accountDtoList;
    }
}
