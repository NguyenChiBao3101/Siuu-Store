package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    Role findByName(String name);


    @Query("SELECT r FROM Role r")
    Set<Role> findAllRolesAsSet();

}
