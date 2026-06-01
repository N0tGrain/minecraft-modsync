package com.n0tgrain.modsyncbackend.dtos.modrinth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModrinthIndexDTO {
    public int formatVersion = 1;
    public String game = "minecraft";
    public String versionId;
    public String name;
    public String summary;
    public List<ModrinthFileDTO> files;
    public Map<String, String> dependencies;

    public ModrinthIndexDTO() {}
}
