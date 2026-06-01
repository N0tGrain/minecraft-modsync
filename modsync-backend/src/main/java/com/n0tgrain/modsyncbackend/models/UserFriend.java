package com.n0tgrain.modsyncbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_friends")
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private CustomUser friend;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserFriend() {}

    public UserFriend(CustomUser user, CustomUser friend) {
        this.user = user;
        this.friend = friend;
        this.createdAt = LocalDateTime.now();
    }
}
