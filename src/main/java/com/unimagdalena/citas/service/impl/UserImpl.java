package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.user.UserRegisterDTO;
import com.unimagdalena.citas.model.Roles;
import com.unimagdalena.citas.model.RolesEnum;
import com.unimagdalena.citas.model.User;
import com.unimagdalena.citas.repository.RolesRepository;
import com.unimagdalena.citas.repository.UserRepository;
import com.unimagdalena.citas.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public UserImpl(UserRepository userRepository, 
                   PasswordEncoder passwordEncoder,
                   RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public User register(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email Ya existe");
        }
        
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        
        Roles role = rolesRepository.findById(dto.roleId())
            .orElseThrow(() -> new RuntimeException("Role no encontrado"));
        
        user.setRole(role);
        return userRepository.save(user);
    }
    public User getUserInfo(long userId){
        return userRepository.findById(userId).get();
    }
}
