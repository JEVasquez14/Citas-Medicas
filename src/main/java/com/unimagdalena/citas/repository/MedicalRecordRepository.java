package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByDiagnosis(String diagnosis);

    @Query("SELECT m FROM MedicalRecord m WHERE m.appointment.id = :appointmentId AND m.patient.id = :patientId")
    Optional<MedicalRecord> findByAppointmentAndPatient(@Param("appointmentId") Long appointmentId, @Param("patientId") Long patientId);

}