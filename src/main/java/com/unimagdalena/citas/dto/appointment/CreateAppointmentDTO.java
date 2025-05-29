package com.unimagdalena.citas.dto.appointment;

import com.unimagdalena.citas.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAppointmentDTO {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long consultRoomId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    private AppointmentStatus status;
}
