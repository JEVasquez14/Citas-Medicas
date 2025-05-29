package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;
import com.unimagdalena.citas.exception.ConflictException;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.AppointmentMapper;
import com.unimagdalena.citas.model.*;
import com.unimagdalena.citas.repository.AppointmentRepository;
import com.unimagdalena.citas.repository.ConsultRoomRepository;
import com.unimagdalena.citas.repository.DoctorRepository;
import com.unimagdalena.citas.repository.PatientRepository;
import com.unimagdalena.citas.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ConsultRoomRepository consultRoomRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponseDTO createAppointment(CreateAppointmentDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        ConsultRoom consultRoom = consultRoomRepository.findById(dto.getConsultRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));


        boolean conflict = hasConflict(dto.getConsultRoomId(), dto.getStartTime(), dto.getEndTime());
        if (conflict) {
            throw new ConflictException("Ya existe una cita en este horario y consultorio");
        }

        Appointment appointment = appointmentMapper.toModel(dto);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setConsultRoom(consultRoom);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, UpdateAppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));
            appointment.setDoctor(doctor);
        }

        if (dto.getPatientId() != null) {
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
            appointment.setPatient(patient);
        }

        if (dto.getConsultRoomId() != null) {
            ConsultRoom consultRoom = consultRoomRepository.findById(dto.getConsultRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));
            appointment.setConsultRoom(consultRoom);
        }

        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            boolean conflict = hasConflict(appointment.getConsultRoom().getId(), dto.getStartTime(), dto.getEndTime());
            if (conflict) {
                throw new ConflictException("Ya existe una cita en este horario y consultorio");
            }
            appointment.setStartTime(dto.getStartTime());
            appointment.setEndTime(dto.getEndTime());
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        appointmentRepository.delete(appointment);
    }

    @Override
    public boolean hasConflict(Long consultRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository.existsConflict(consultRoomId, startTime, endTime);
    }


    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, startOfDay, endOfDay);
        return appointments.stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
