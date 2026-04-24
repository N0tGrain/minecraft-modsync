package com.n0tgrain.modsyncbackend.util;

import com.n0tgrain.modsyncbackend.models.Role;
import com.n0tgrain.modsyncbackend.repositories.RoleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private final RoleRepository roleRepository;

    public Seeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        this.seedRoles();
    }

    private void seedRoles() {
        Role userRole = new Role("ROLE_USER", 1L);
        Role adminRole = new Role("ROLE_ADMIN", 2L);

        this.roleRepository.save(userRole);
        this.roleRepository.save(adminRole);
    }
}
