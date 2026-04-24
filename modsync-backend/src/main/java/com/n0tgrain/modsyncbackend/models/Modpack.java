package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Modpack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String minecraftVersion;

    @OneToMany(mappedBy = "modpack", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ModpackMod> mods;

    public Modpack() {}

    public Modpack(String name, String description, String minecraftVersion, List<ModpackMod> mods) {
        this.name = name;
        this.description = description;
        this.minecraftVersion = minecraftVersion;
        this.mods = mods;
    }
}
