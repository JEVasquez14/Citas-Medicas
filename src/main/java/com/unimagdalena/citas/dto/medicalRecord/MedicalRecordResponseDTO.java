package com.unimagdalena.citas.dto.medicalRecord;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {

    private Long id;

    private Long appointmentId;

    private Long patientId;

    private String diagnosis;

    private String notes;

    private LocalDateTime createdAt;
}
