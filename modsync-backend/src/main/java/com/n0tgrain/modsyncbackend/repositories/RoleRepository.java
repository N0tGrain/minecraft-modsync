package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
