package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<CustomUser> findByUsername(String username);

}
