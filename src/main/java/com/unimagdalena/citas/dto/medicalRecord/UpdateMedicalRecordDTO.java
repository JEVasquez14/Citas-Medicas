package com.unimagdalena.citas.dto.medicalRecord;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMedicalRecordDTO {

    @NotNull
    private String diagnosis;

    private String notes;
}