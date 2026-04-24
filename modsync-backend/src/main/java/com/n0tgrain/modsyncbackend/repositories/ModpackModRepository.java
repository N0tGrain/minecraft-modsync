package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.ModpackMod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModpackModRepository extends JpaRepository<ModpackMod, Long> {}
