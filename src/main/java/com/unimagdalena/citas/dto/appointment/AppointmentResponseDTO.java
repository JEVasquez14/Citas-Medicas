package com.unimagdalena.citas.dto.appointment;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppointmentResponseDTO {

    private Long id;

    private String patientName;

    private String doctorName;

    private String consultRoomName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;
}