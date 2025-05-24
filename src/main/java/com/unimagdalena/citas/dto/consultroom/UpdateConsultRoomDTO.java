package com.unimagdalena.citas.dto.consultroom;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateConsultRoomDTO {

    @NotNull
    private String name;

    @NotNull
    private Integer floor;

    @NotNull
    private String description;
}