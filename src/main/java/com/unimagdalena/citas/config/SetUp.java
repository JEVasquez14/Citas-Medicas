package com.unimagdalena.citas.config;

import com.unimagdalena.citas.model.Roles;
import com.unimagdalena.citas.model.RolesEnum;
import com.unimagdalena.citas.model.User;
import com.unimagdalena.citas.repository.RolesRepository;
import com.unimagdalena.citas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class SetUp implements ApplicationListener<ContextRefreshedEvent>{

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetUp(RolesRepository rolesRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    void createRoleIfNotExist(RolesEnum role) {

        Optional<Roles> roles = rolesRepository.findByRole(role);

        if(roles.isEmpty()) {
            Roles roles1 = new Roles();
            roles1.setRole(role);
            rolesRepository.save(roles1);
        }
        System.out.println("Role found: " + role.getRole());
    }

    @Transactional
    public void createAdmin(){
        User user = new User();
        if(!userRepository.existsByEmail("admin@admin.com")){
            Optional<Roles> roles = rolesRepository.findByRole(RolesEnum.ADMIN);
            if(roles.isPresent()){
                user.setRole(roles.get());
                user.setEmail("admin@admin.com");
                user.setPassword(passwordEncoder.encode("admin"));
                userRepository.save(user);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRoleIfNotExist(RolesEnum.ADMIN);
        createRoleIfNotExist(RolesEnum.DOCTOR);
        createAdmin();
    }



}
