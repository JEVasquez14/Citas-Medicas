package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.doctor.CreateDoctorDTO;
import com.unimagdalena.citas.dto.doctor.DoctorResponseDTO;
import com.unimagdalena.citas.dto.doctor.UpdateDoctorDTO;
import com.unimagdalena.citas.mapper.DoctorMapper;
import com.unimagdalena.citas.model.Doctor;
import com.unimagdalena.citas.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorImplTest {

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorImpl doctorImpl;

    @Test
    void createDoctor() {
        CreateDoctorDTO createDTO = CreateDoctorDTO.builder()
                .fullName("Dra. Camila Reyes")
                .email("camila.reyes@clinic.com")
                .specialty("Dermatología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(14, 0))
                .build();

        Doctor doctor = Doctor.builder()
                .id(1L)
                .fullName(createDTO.getFullName())
                .email(createDTO.getEmail())
                .specialty(createDTO.getSpecialty())
                .availableFrom(createDTO.getAvailableFrom())
                .availableTo(createDTO.getAvailableTo())
                .build();

        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Dra. Camila Reyes")
                .email("camila.reyes@clinic.com")
                .specialty("Dermatología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(14, 0))
                .build();

        when(doctorMapper.toModel(createDTO)).thenReturn(doctor);
        when(doctorRepository.findByFullName(createDTO.getFullName())).thenReturn(Optional.empty());
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toResponse(doctor)).thenReturn(responseDTO);

        DoctorResponseDTO result = doctorImpl.createDoctor(createDTO);

        assertNotNull(result);
        assertEquals("Dra. Camila Reyes", result.getFullName());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void updateDoctor() {
        Long doctorId = 1L;
        UpdateDoctorDTO updateDTO = UpdateDoctorDTO.builder()
                .fullName("Dra. Camila Reyes Actualizada")
                .email("camila.reyes.upd@clinic.com")
                .specialty("Pediatría")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        Doctor existingDoctor = Doctor.builder()
                .id(doctorId)
                .fullName("Dra. Camila Reyes")
                .email("camila.reyes@clinic.com")
                .specialty("Dermatología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(14, 0))
                .build();

        Doctor updatedDoctor = Doctor.builder()
                .id(doctorId)
                .fullName(updateDTO.getFullName())
                .email(updateDTO.getEmail())
                .specialty(updateDTO.getSpecialty())
                .availableFrom(updateDTO.getAvailableFrom())
                .availableTo(updateDTO.getAvailableTo())
                .build();

        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
                .id(doctorId)
                .fullName(updateDTO.getFullName())
                .email(updateDTO.getEmail())
                .specialty(updateDTO.getSpecialty())
                .availableFrom(updateDTO.getAvailableFrom())
                .availableTo(updateDTO.getAvailableTo())
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(updatedDoctor)).thenReturn(updatedDoctor);
        when(doctorMapper.toResponse(updatedDoctor)).thenReturn(responseDTO);

        DoctorResponseDTO result = doctorImpl.updateDoctor(doctorId, updateDTO);

        assertNotNull(result);
        assertEquals("Dra. Camila Reyes Actualizada", result.getFullName());
        assertEquals("Pediatría", result.getSpecialty());
        verify(doctorRepository).save(updatedDoctor);
    }

    @Test
    void deleteDoctor() {
        Long doctorId = 1L;

        Doctor existingDoctor = Doctor.builder()
                .id(doctorId)
                .fullName("Dra. Camila Reyes")
                .email("camila.reyes@clinic.com")
                .specialty("Dermatología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(14, 0))
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));

        doctorImpl.deleteDoctor(doctorId);

        verify(doctorRepository, times(1)).deleteById(doctorId);
    }

    @Test
    void getDoctorById() {
        Long doctorId = 1L;
        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .fullName("Dr. John Doe")
                .email("john.doe@example.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .appointments(new ArrayList<>())
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                .id(doctorId)
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .specialty(doctor.getSpecialty())
                .availableFrom(doctor.getAvailableFrom())
                .availableTo(doctor.getAvailableTo())
                .build();
        when(doctorMapper.toResponse(doctor)).thenReturn(doctorResponseDTO);

        DoctorResponseDTO result = doctorImpl.getDoctorById(doctorId);

        assertNotNull(result);
        assertEquals(doctorId, result.getId());
        assertEquals("Dr. John Doe", result.getFullName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Cardiology", result.getSpecialty());
        assertEquals(LocalTime.of(8, 0), result.getAvailableFrom());
        assertEquals(LocalTime.of(16, 0), result.getAvailableTo());

        verify(doctorRepository).findById(doctorId);
        verify(doctorMapper).toResponse(doctor);
    }

    @Test
    void getAllDoctors() {
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .fullName("Dr. John Doe")
                .email("john.doe@example.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .appointments(new ArrayList<>())
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .fullName("Dr. Jane Smith")
                .email("jane.smith@example.com")
                .specialty("Neurology")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .appointments(new ArrayList<>())
                .build();

        List<Doctor> doctorList = List.of(doctor1, doctor2);
        when(doctorRepository.findAll()).thenReturn(doctorList);

        DoctorResponseDTO doctorResponseDTO1 = DoctorResponseDTO.builder()
                .id(doctor1.getId())
                .fullName(doctor1.getFullName())
                .email(doctor1.getEmail())
                .specialty(doctor1.getSpecialty())
                .availableFrom(doctor1.getAvailableFrom())
                .availableTo(doctor1.getAvailableTo())
                .build();

        DoctorResponseDTO doctorResponseDTO2 = DoctorResponseDTO.builder()
                .id(doctor2.getId())
                .fullName(doctor2.getFullName())
                .email(doctor2.getEmail())
                .specialty(doctor2.getSpecialty())
                .availableFrom(doctor2.getAvailableFrom())
                .availableTo(doctor2.getAvailableTo())
                .build();

        when(doctorMapper.toResponse(doctor1)).thenReturn(doctorResponseDTO1);
        when(doctorMapper.toResponse(doctor2)).thenReturn(doctorResponseDTO2);

        List<DoctorResponseDTO> result = doctorImpl.getAllDoctors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(doctor1.getId(), result.get(0).getId());
        assertEquals(doctor2.getId(), result.get(1).getId());

        verify(doctorRepository).findAll();
        verify(doctorMapper).toResponse(doctor1);
        verify(doctorMapper).toResponse(doctor2);
    }

    @Test
    void getDoctorsBySpecialty() {
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .fullName("Dr. John Doe")
                .email("john.doe@example.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .appointments(new ArrayList<>())
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .fullName("Dr. Jane Smith")
                .email("jane.smith@example.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .appointments(new ArrayList<>())
                .build();


        List<Doctor> doctorList = List.of(doctor1, doctor2);
        when(doctorRepository.findBySpecialty("Cardiology")).thenReturn(doctorList);

        DoctorResponseDTO doctorResponseDTO1 = DoctorResponseDTO.builder()
                .id(doctor1.getId())
                .fullName(doctor1.getFullName())
                .email(doctor1.getEmail())
                .specialty(doctor1.getSpecialty())
                .availableFrom(doctor1.getAvailableFrom())
                .availableTo(doctor1.getAvailableTo())
                .build();

        DoctorResponseDTO doctorResponseDTO2 = DoctorResponseDTO.builder()
                .id(doctor2.getId())
                .fullName(doctor2.getFullName())
                .email(doctor2.getEmail())
                .specialty(doctor2.getSpecialty())
                .availableFrom(doctor2.getAvailableFrom())
                .availableTo(doctor2.getAvailableTo())
                .build();

        when(doctorMapper.toResponse(doctor1)).thenReturn(doctorResponseDTO1);
        when(doctorMapper.toResponse(doctor2)).thenReturn(doctorResponseDTO2);

        List<DoctorResponseDTO> result = doctorImpl.getDoctorsBySpecialty("Cardiology");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(doctor1.getId(), result.get(0).getId());
        assertEquals(doctor2.getId(), result.get(1).getId());

        verify(doctorRepository).findBySpecialty("Cardiology");

        verify(doctorMapper).toResponse(doctor1);
        verify(doctorMapper).toResponse(doctor2);
    }
}