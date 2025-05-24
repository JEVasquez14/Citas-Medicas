package com.unimagdalena.citas.dto.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePatientDTO {

    @NotNull
    private String fullName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String phone;
}