package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail, String> {
}
