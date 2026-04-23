package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    @OneToMany(mappedBy = "mod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ModVersion> versions;

}
