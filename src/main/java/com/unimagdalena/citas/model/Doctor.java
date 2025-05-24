package com.unimagdalena.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String specialty;

    @NotNull
    private LocalTime availableFrom;

    @NotNull
    private LocalTime availableTo;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}

