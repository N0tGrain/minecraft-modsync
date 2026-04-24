package com.n0tgrain.modsyncbackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.n0tgrain.modsyncbackend.models.CustomUser;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("select u from CustomUser u join fetch u.role where u.username = :username")
    Optional<CustomUser> findByUsername(@Param("username") String username);

}
