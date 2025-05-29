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

    private Long patientId;

    private Long doctorId;

    private AppointmentStatus status;

    private Long consultRoomId;

    @FutureOrPresent
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;
}
