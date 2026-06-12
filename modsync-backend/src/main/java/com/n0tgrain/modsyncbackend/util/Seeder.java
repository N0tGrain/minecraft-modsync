package com.n0tgrain.modsyncbackend.util;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.Mod;
import com.n0tgrain.modsyncbackend.models.Role;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import com.n0tgrain.modsyncbackend.repositories.ModRepository;
import com.n0tgrain.modsyncbackend.repositories.RoleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder {

    private final RoleRepository roleRepository;
    private final ModRepository modRepository;
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Seeder(RoleRepository roleRepository, ModRepository modRepository, CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.modRepository = modRepository;
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
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

        this.customUserRepository.save(new CustomUser("N0tBaguette", "N0tGrain@outlook.com", passwordEncoder.encode("Test123!"), userRole));
        this.customUserRepository.save(new CustomUser("N0tBaguette_", "N0tGrain2@outlook.com", passwordEncoder.encode("Test123!"), userRole));

        Mod testMod = new Mod();
        List<String> categories = new ArrayList<>();
        categories.add("category 1");
        categories.add("category 2");
        testMod.setCategories(categories);
        testMod.setDescription("This is a test mod");
        testMod.setName("Test Mod");
        testMod.setIconUrl("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExZ2IxaTV3eDZndnNtcm56MDB6emh4dzU1dXBmdDQ5d3ZjY204cmlsdyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/sthmCnCpfr8M8jtTQy/giphy.gif");
        testMod.setExternalId("LNytGWDc");
        testMod.setSlug("yap yap");
        testMod.setDownloads(42069);
        this.modRepository.save(testMod);
    }
}
