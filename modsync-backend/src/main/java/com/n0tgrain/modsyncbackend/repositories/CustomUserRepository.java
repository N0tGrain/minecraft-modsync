package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
}
