package com.unimagdalena.citas.dto.consultroom;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultRoomResponseDTO {

    private Long id;

    private String name;

    private Integer floor;

    private String description;
}