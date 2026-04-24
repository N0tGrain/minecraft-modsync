package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.ModVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModVersionRepository extends JpaRepository<ModVersion, Long> {}
