package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByAccount(Account account);
}
