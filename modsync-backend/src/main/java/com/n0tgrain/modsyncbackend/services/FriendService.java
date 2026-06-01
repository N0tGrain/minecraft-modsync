package com.n0tgrain.modsyncbackend.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.n0tgrain.modsyncbackend.dtos.FriendDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.UserFriend;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import com.n0tgrain.modsyncbackend.repositories.UserFriendRepository;

import java.util.List;

@Service
public class FriendService {

    private final UserFriendRepository userFriendRepository;
    private final CustomUserRepository customUserRepository;

    public FriendService(UserFriendRepository userFriendRepository, CustomUserRepository customUserRepository) {
        this.userFriendRepository = userFriendRepository;
        this.customUserRepository = customUserRepository;
    }

    public void addFriend(Long friendId) {
        CustomUser currentUser = getAuthenticatedUser();
        
        if (currentUser.getId().equals(friendId)) {
            throw new CustomUserException("Cannot add yourself as a friend");
        }

        CustomUser friend = customUserRepository.findById(friendId)
                .orElseThrow(() -> new CustomUserException("User not found"));

        // Check if friendship already exists
        if (userFriendRepository.areFriends(currentUser.getId(), friendId)) {
            throw new CustomUserException("Already friends with this user");
        }

        // Create friendship
        UserFriend userFriend = new UserFriend(currentUser, friend);
        userFriendRepository.save(userFriend);
    }

    public void removeFriend(Long friendId) {
        CustomUser currentUser = getAuthenticatedUser();
        userFriendRepository.deleteByUserIdAndFriendId(currentUser.getId(), friendId);
    }

    public List<FriendDTO> getFriends() {
        CustomUser currentUser = getAuthenticatedUser();
        
        return userFriendRepository.findByUserId(currentUser.getId()).stream()
                .map(uf -> {
                    FriendDTO dto = new FriendDTO();
                    dto.id = uf.getFriend().getId();
                    dto.username = uf.getFriend().getUsername();
                    dto.email = uf.getFriend().getEmail();
                    return dto;
                })
                .toList();
    }

    public boolean areFriendsWith(Long userId, Long targetUserId) {
        return userFriendRepository.areFriends(userId, targetUserId);
    }

    private CustomUser getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new CustomUserException("Unauthorized");
        }
        if (!(authentication.getPrincipal() instanceof CustomUser user)) {
            throw new CustomUserException("Invalid authentication principal");
        }
        return user;
    }
}
