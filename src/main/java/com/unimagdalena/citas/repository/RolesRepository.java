package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.Roles;
import com.unimagdalena.citas.model.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRole(RolesEnum role);

}
