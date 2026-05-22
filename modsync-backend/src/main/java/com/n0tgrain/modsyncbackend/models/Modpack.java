package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Modpack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;
    private String minecraftVersion;
    private String loader;
    private boolean isPublic = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private CustomUser owner;

    @OneToMany(mappedBy = "modpack", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ModpackMod> mods = new ArrayList<>();

    public Modpack() {}

    public Modpack(String name, String description, String minecraftVersion, String loader, boolean isPublic, CustomUser owner, List<ModpackMod> mods) {
        this.name = name;
        this.description = description;
        this.minecraftVersion = minecraftVersion;
        this.loader = loader;
        this.isPublic = isPublic;
        this.owner = owner;
        this.mods = mods;
    }
}
