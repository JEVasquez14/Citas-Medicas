package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.Appointment;
import com.unimagdalena.citas.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    Appointment findByIdAndStatus(Long id,  AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.startTime >= :startOfDay AND a.startTime < :endOfDay")
    List<Appointment> findByDoctorIdAndDate(
            @Param("doctorId") Long doctorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.consultRoom.id = :consultRoomId AND a.status <> 'CANCELLED' AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    boolean existsConflict(@Param("consultRoomId") Long consultRoomId,
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime);
}