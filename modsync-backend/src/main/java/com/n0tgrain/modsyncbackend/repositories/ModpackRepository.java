package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.Modpack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModpackRepository extends JpaRepository<Modpack, Integer> {}
