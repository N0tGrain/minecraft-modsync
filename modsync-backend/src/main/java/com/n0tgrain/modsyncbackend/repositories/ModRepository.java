package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModRepository extends JpaRepository<Mod, Long> {
    boolean existsByExternalId(String externalId);
}
