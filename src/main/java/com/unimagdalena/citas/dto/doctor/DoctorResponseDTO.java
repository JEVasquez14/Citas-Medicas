package com.unimagdalena.citas.dto.doctor;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {

    private Long id;

    private String fullName;

    private String email;

    private String specialty;

    private LocalTime availableFrom;

    private LocalTime availableTo;
}