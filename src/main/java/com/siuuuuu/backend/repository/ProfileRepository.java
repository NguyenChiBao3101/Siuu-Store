package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    // Tìm Profile bằng Account ID
    Profile findByAccount_Id(String accountId);

    // Tìm Profile bằng Email của Account
    Profile findByAccount_Email(String email);

}
