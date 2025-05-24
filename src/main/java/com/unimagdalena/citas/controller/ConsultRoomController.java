package com.unimagdalena.citas.controller;

import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;
import com.unimagdalena.citas.service.ConsultRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ConsultRoomController {

    private final ConsultRoomService consultRoomService;

    @GetMapping
    public ResponseEntity<List<ConsultRoomResponseDTO>> getAllRooms() {
        List<ConsultRoomResponseDTO> rooms = consultRoomService.getAllConsultRooms();
        System.out.println("entrando");
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultRoomResponseDTO> getRoomById(@PathVariable Long id) {
        ConsultRoomResponseDTO room = consultRoomService.getConsultRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PostMapping
    public ResponseEntity<ConsultRoomResponseDTO> createRoom(@Valid @RequestBody CreateConsultRoomDTO dto) {
        ConsultRoomResponseDTO createdRoom = consultRoomService.createConsultRoom(dto);
        return ResponseEntity.status(201).body(createdRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultRoomResponseDTO> updateRoom(@PathVariable Long id, @Valid @RequestBody UpdateConsultRoomDTO dto) {
        ConsultRoomResponseDTO updatedRoom = consultRoomService.updateConsultRoom(id, dto);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        consultRoomService.deleteConsultRoom(id);
        return ResponseEntity.noContent().build();
    }
}
