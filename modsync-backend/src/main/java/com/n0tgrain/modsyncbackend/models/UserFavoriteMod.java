package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "user_favorite_mods",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "mod_id"})
)
public class UserFavoriteMod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_id", nullable = false)
    private Mod mod;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserFavoriteMod() {}

    public UserFavoriteMod(CustomUser user, Mod mod) {
        this.user = user;
        this.mod = mod;
        this.createdAt = LocalDateTime.now();
    }
}
