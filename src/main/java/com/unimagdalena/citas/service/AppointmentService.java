package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(CreateAppointmentDTO dto);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO updateAppointment(Long id, UpdateAppointmentDTO dto);

    void deleteAppointment(Long id);

    boolean hasConflict(Long consultRoomId, LocalDateTime startTime, LocalDateTime endTime);

    List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);

}