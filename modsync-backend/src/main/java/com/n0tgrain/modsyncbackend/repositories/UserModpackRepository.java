package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.UserModpack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModpackRepository extends JpaRepository<UserModpack, Long> {}
