package com.unimagdalena.citas.dto.patient;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {

    private Long id;

    private String fullName;

    private String email;

    private String phone;
}