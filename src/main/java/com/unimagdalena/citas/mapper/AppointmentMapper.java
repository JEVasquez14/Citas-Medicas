package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;
import com.unimagdalena.citas.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toModel(CreateAppointmentDTO dto);

    Appointment toModel(UpdateAppointmentDTO dto);

    AppointmentResponseDTO toResponse(Appointment appointment);
}