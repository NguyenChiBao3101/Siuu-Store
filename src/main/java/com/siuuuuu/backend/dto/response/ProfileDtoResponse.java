package com.siuuuuu.backend.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDtoResponse {
    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 15)
    String phoneNumber;
}
