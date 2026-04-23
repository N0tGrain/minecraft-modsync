package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

@Entity
public class ModVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String version;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_id")
    private Mod mod;

}
