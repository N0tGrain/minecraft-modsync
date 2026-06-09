package com.n0tgrain.modsyncbackend.util;

import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.models.Role;
import com.n0tgrain.modsyncbackend.repositories.ModRepository;
import com.n0tgrain.modsyncbackend.repositories.RoleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder {

    private final RoleRepository roleRepository;
    private final ModRepository modRepository;

    public Seeder(RoleRepository roleRepository, ModRepository modRepository) {
        this.roleRepository = roleRepository;
        this.modRepository = modRepository;
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

        Mod testMod = new Mod();
        List<String> categories = new ArrayList<>();
        categories.add("category 1");
        categories.add("category 2");
        testMod.setCategories(categories);
        testMod.setDescription("This is a test mod");
        testMod.setName("Test Mod");
        testMod.setIconUrl("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExZ2IxaTV3eDZndnNtcm56MDB6emh4dzU1dXBmdDQ5d3ZjY204cmlsdyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/sthmCnCpfr8M8jtTQy/giphy.gif");
        testMod.setExternalId("yapyap2035982356834yapyap");
        testMod.setSlug("yap yap");
        testMod.setDownloads(42069);
        this.modRepository.save(testMod);
    }
}
