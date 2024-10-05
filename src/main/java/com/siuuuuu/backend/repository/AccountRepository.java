package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);

    boolean existsByEmail(String email);

}
