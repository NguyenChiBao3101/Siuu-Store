package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, String> {
   List<Size> findAll();
}
