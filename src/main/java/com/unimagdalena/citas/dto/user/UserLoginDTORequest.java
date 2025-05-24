package com.unimagdalena.citas.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTORequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Password cannot be blank")
        //@Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {
}
