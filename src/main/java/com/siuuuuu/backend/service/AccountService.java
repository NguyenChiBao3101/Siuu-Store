package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.Roles;
import com.siuuuuu.backend.dto.request.*;
import com.siuuuuu.backend.dto.response.AccountDtoResponse;
import com.siuuuuu.backend.dto.response.ProfileDtoResponse;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private ProfileService profileService;

    @Autowired
    private PasswordEncoder plainPasswordEncoder;

    public AccountService(
            @Qualifier("plainPasswordEncoder") PasswordEncoder plainPasswordEncoder) {
        this.plainPasswordEncoder = plainPasswordEncoder;
    }


    public List<AccountDtoResponse> findAllAccounts() {
        return mapToListDto(accountRepository.findAll());
    }

    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        return account;
    }

    //Get account is employee
    public Page<AccountDtoResponse> getEmployeeAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Account> accountDtoPage = accountRepository.findByRoleEmployee(pageable);
        return accountDtoPage.map(this::mapToDto);
    }

    public Page<AccountDtoResponse> getEmployeeByStatus(int page, int size, boolean status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Account> accountDtoPage = accountRepository.findByRoleEmployeeAndIsActive(status, pageable);
        return accountDtoPage.map(this::mapToDto);
    }

    @Transactional
    public AccountDtoResponse register(RegisterDto registerDto) {
        // Check input
        if (accountRepository.existsByEmail(registerDto.getEmail())) {
            throw new NoSuchElementException("Tài khoản email đã tồn tại");
        }
        Account newAccount = new Account();
        newAccount.setEmail(registerDto.getEmail());
        newAccount.setPassword(plainPasswordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("EMPLOYEE"));

        newAccount.setRoles(roles);
        newAccount.setIsActive(true);

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

    public AccountDtoResponse updateAccount(ProfileDto profileDto, String email) {

        // Update account's information
        Account currentAccount = accountRepository.findByEmail(email);


        if (currentAccount.getIsActive() != null) {
            currentAccount.setIsActive(profileDto.getIsActive());
        }
        Account newAccount = accountRepository.save(currentAccount);

        // update currentProfile's information
        Profile currentProfile = profileRepository.findByAccount_Email(email);
        if (currentProfile.getFirstName() != null) {
            currentProfile.setFirstName(profileDto.getFirstName());
        }
        if (currentProfile.getLastName() != null) {
            currentProfile.setLastName(profileDto.getLastName());
        }
        if (currentProfile.getDateOfBirth() != null) {
            currentProfile.setDateOfBirth(profileDto.getDateOfBirth());
        }
        if (currentProfile.getPhoneNumber() != null) {
            currentProfile.setPhoneNumber(profileDto.getPhoneNumber());
        }
        if (currentProfile.getIsActive() != null) {
            currentProfile.setIsActive(profileDto.getIsActive());
        }
        if (currentProfile.getAccount() != null) {
            currentProfile.setAccount(newAccount);
        }
        profileRepository.save(currentProfile);

        return mapToDto(newAccount);
    }

    // updated account api

    public AccountDtoResponse updateAccountAPI(UpdateAccountStatusDto updateAccountStatusDto, String email) {
        Account currentAccount = accountRepository.findByEmail(email);

        if (currentAccount.getIsActive() != null) {
            currentAccount.setIsActive(updateAccountStatusDto.getIsActive());
        }
        Account newAccount = accountRepository.save(currentAccount);
        return mapToDto(newAccount);
    }

    public ProfileDtoResponse getProfileAPI(String email) {
        Account currentAccount = accountRepository.findByEmail(email);
        if (!currentAccount.getIsActive()) {
            throw new BadCredentialsException("Tài khoản đang bị vô hiệu hoá");
        }
        Profile profile = currentAccount.getProfile();
        return profileService.mapToDto(profile);
    }

    public ProfileDtoResponse updateProfileAPI(UpdateProfileDto updateProfileDto, String email) {
        Account currentAccount = accountRepository.findByEmail(email);

        Profile profile = currentAccount.getProfile();
        profile.setFirstName(updateProfileDto.getFirstName());
        profile.setLastName(updateProfileDto.getLastName());
        profile.setDateOfBirth(updateProfileDto.getDateOfBirth());
        profile.setPhoneNumber(updateProfileDto.getPhoneNumber());

        accountRepository.save(currentAccount);
        return profileService.mapToDto(currentAccount.getProfile());
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new NoSuchElementException("Không tìm thấy tài khoản");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới cần khác so với mật khẩu hiện tại");
        }

        if (!plainPasswordEncoder.matches(oldPassword, account.getPassword())) {
            throw new BadCredentialsException("Mật khẩu hiện tại không chính xác");
        }

        account.setPassword(plainPasswordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public AccountDtoResponse updateRoles(UpdateRolesRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail());
        if (account == null) {
            throw new NoSuchElementException("Không tìm thấy tài khoản với email: " + request.getEmail());
        }
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Roles không được để trống");
        }
        //upperCase for input
        Set<String> normalized = request.getRoles().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        //Set input with Roles
        Set<String> allowed = Arrays.stream(Roles.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        normalized.retainAll(allowed);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy tên roles hợp lệ trong yêu cầu");
        }

        Set<Role> persistedRoles = normalized.stream()
                .map(name -> {
                    Role existing = roleRepository.findByName(name);
                    return (existing != null) ? existing : roleRepository.save(new Role(name));
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        account.setRoles(persistedRoles);
        Account saved = accountRepository.save(account);

        return mapToDto(saved);
    }

    public AccountDtoResponse mapToDto(Account account) {
        AccountDtoResponse accountDtoResponse = new AccountDtoResponse();
        accountDtoResponse.setEmail(account.getEmail());
        accountDtoResponse.setPassword(account.getPassword());
        accountDtoResponse.setIsActive(account.getIsActive());
        accountDtoResponse.setIsVerified(account.getIsVerified());
        accountDtoResponse.setRoles(account.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        accountDtoResponse.setProfile(profileService.mapToDto(account.getProfile()));
        return accountDtoResponse;
    }

    public List<AccountDtoResponse> mapToListDto(List<Account> accountList) {
        List<AccountDtoResponse> accountDtoList = new ArrayList<>();
        for (Account account : accountList) {
            accountDtoList.add(mapToDto(account));
        }
        return accountDtoList;
    }
}
