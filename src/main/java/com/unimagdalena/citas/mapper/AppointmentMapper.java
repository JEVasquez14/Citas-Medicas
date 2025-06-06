package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;
import com.unimagdalena.citas.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toModel(CreateAppointmentDTO dto);

    Appointment toModel(UpdateAppointmentDTO dto);

    @Mapping(source = "doctor.fullName", target = "doctorName")
    @Mapping(source = "patient.fullName", target = "patientName")
    @Mapping(source = "consultRoom.name", target = "consultRoomName")
    AppointmentResponseDTO toResponse(Appointment appointment);
}