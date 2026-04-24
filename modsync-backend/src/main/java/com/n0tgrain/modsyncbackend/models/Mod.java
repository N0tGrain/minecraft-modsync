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

    @Column(unique = true)
    private String externalId; // this is the project id!

    private String name;
    private String slug;
    private String description;
    private String iconUrl;

    private Integer downloads;

    @ElementCollection
    private List<String> categories;

    @OneToMany(mappedBy = "mod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ModVersion> versions;

    public Mod() {}

    public Mod(String externalId, String name, String slug, String description, String iconUrl, Integer downloads, List<String> categories, List<ModVersion> versions) {
        this.externalId = externalId;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.iconUrl = iconUrl;
        this.downloads = downloads;
        this.categories = categories;
        this.versions = versions;
    }
}
