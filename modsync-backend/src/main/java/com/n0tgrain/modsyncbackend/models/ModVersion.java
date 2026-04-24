package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ModVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String version;
    private String fileUrl;
    private Date releaseDate;
    private Long curseforgeFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_id")
    private Mod mod;

    public ModVersion() {}

    public ModVersion(String version, String fileUrl, Date releaseDate, Long curseforgeFileId, Mod mod) {
        this.version = version;
        this.fileUrl = fileUrl;
        this.releaseDate = releaseDate;
        this.curseforgeFileId = curseforgeFileId;
        this.mod = mod;
    }
}
