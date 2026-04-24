package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Mod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long curseforgeProjectId;

    @OneToMany(mappedBy = "mod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ModVersion> versions;

}
