package com.n0tgrain.modsyncbackend.dtos.modrinth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModrinthFileDTO {
    public String path;
    public Map<String, String> hashes;
    public ModrinthEnvDTO env;
    public String[] downloads;
    public long fileSize;

    public ModrinthFileDTO() {}

    public ModrinthFileDTO(String path, Map<String, String> hashes, String[] downloads, long fileSize) {
        this.path = path;
        this.hashes = hashes;
        this.downloads = downloads;
        this.fileSize = fileSize;
    }
}
