package com.unimagdalena.citas.service.impl;



import com.unimagdalena.citas.dto.token.TokenDTOResponse;
import com.unimagdalena.citas.dto.user.UserLoginDTORequest;
import com.unimagdalena.citas.exception.UserLoginException;
import com.unimagdalena.citas.mapper.TokenMapper;
import com.unimagdalena.citas.model.User;
import com.unimagdalena.citas.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenMapper tokenMapper;

    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, TokenMapper tokenMapper) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenMapper = tokenMapper;
    }

    public TokenDTOResponse login(UserLoginDTORequest input){

        Optional<User> user = userRepository.findByEmail(input.email());

        if(user.isEmpty() || !passwordEncoder.matches(input.password(), user.get().getPassword())){
            throw new UserLoginException("User or password incorrect");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );


        return tokenMapper.toDTO(jwtService.getToken(user.get()), user.get().getRole().getRole().name());
    }


}