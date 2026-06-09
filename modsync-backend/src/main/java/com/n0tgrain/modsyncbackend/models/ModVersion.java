package com.n0tgrain.modsyncbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class ModVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String externalVersionId;
    private String version;
    private String minecraftVersion;
    private String loader;
    private String fileUrl;
    private Date releaseDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_id")
    private Mod mod;

    public ModVersion() {}

    public ModVersion(String externalVersionId, String version, String minecraftVersion, String loader, String fileUrl, Date releaseDate, Mod mod) {
        this.externalVersionId = externalVersionId;
        this.version = version;
        this.minecraftVersion = minecraftVersion;
        this.loader = loader;
        this.fileUrl = fileUrl;
        this.releaseDate = releaseDate;
        this.mod = mod;
    }
}
