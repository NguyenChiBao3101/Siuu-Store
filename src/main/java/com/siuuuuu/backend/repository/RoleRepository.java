package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    Role findByName(String name);
}
