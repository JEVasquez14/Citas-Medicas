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
public class UpdateAppointmentDTO {

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private AppointmentStatus status;

    @NotNull
    private Long consultRoomId;

    @FutureOrPresent
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;
}