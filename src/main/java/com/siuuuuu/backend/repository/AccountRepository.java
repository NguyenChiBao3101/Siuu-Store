package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.dto.request.RegisterDto;
import com.siuuuuu.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);

    boolean existsByEmail(String email);

    List<Account> findByIsActive(Boolean isActive);

}
