package com.unimagdalena.citas.controller;

import com.unimagdalena.citas.dto.token.TokenDTOResponse;
import com.unimagdalena.citas.dto.user.UserLoginDTORequest;
import com.unimagdalena.citas.dto.user.UserRegisterDTO;
import com.unimagdalena.citas.service.UserService;
import com.unimagdalena.citas.service.impl.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO registerDetails) {
        logger.info("Recibida solicitud de registro para el email: {}", registerDetails.email());
        userService.register(registerDetails);
        logger.info("Usuario registrado exitosamente: {}", registerDetails.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTOResponse> login(@RequestBody UserLoginDTORequest loginDetails) {
        logger.info("Recibida solicitud de login para el email: {}", loginDetails.email());
        TokenDTOResponse response = authService.login(loginDetails);
        logger.info("Login exitoso para el usuario: {}", loginDetails.email());
        return ResponseEntity.ok(response);
    }
}
