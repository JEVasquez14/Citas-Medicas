package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.ConsultRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultRoomRepository extends JpaRepository<ConsultRoom, Long> {

    Optional<ConsultRoom> findByName(String name);

    List<ConsultRoom> findByFloor(Integer floor);

    @Query("SELECT c FROM ConsultRoom c WHERE c.id NOT IN (" +
            "SELECT a.consultRoom.id FROM Appointment a " +
            "WHERE a.startTime < :endTime AND a.endTime > :startTime)")
    List<ConsultRoom> findAvailableConsultRooms(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}