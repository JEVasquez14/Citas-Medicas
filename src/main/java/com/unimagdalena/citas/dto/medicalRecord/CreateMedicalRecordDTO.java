package com.unimagdalena.citas.dto.medicalRecord;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMedicalRecordDTO {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;

    @NotNull
    private String diagnosis;

    private String notes;
}
