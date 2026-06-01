package com.n0tgrain.modsyncbackend.dtos.modrinth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModrinthEnvDTO {
    public String client;
    public String server;

    public ModrinthEnvDTO() {}

    public ModrinthEnvDTO(String client, String server) {
        this.client = client;
        this.server = server;
    }
}
