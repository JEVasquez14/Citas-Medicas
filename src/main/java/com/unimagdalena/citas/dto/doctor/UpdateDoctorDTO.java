package com.unimagdalena.citas.dto.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDoctorDTO {

    @NotNull
    private String fullName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String specialty;

    @NotNull
    private LocalTime availableFrom;

    @NotNull
    private LocalTime availableTo;
}