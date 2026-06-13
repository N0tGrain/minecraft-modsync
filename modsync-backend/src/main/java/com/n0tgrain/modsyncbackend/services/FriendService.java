package com.n0tgrain.modsyncbackend.services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.n0tgrain.modsyncbackend.dtos.FriendDTO;
import com.n0tgrain.modsyncbackend.dtos.FriendRequestCountDTO;
import com.n0tgrain.modsyncbackend.dtos.FriendRequestDTO;
import com.n0tgrain.modsyncbackend.exceptions.CustomUserException;
import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.models.FriendRequest;
import com.n0tgrain.modsyncbackend.models.UserFriend;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;
import com.n0tgrain.modsyncbackend.repositories.FriendRequestRepository;
import com.n0tgrain.modsyncbackend.repositories.UserFriendRepository;

import java.util.List;

@Service
public class FriendService {

    private final UserFriendRepository userFriendRepository;
    private final CustomUserRepository customUserRepository;
    private final FriendRequestRepository friendRequestRepository;

    public FriendService(UserFriendRepository userFriendRepository,
                         CustomUserRepository customUserRepository,
                         FriendRequestRepository friendRequestRepository) {
        this.userFriendRepository = userFriendRepository;
        this.customUserRepository = customUserRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    @Transactional
    public void sendFriendRequest(Long receiverId) {
        CustomUser sender = getAuthenticatedUser();

        if (sender.getId().equals(receiverId)) {
            throw new CustomUserException("Cannot send a friend request to yourself");
        }

        CustomUser receiver = customUserRepository.findById(receiverId)
                .orElseThrow(() -> new CustomUserException("User not found"));

        if (userFriendRepository.areFriends(sender.getId(), receiverId)) {
            throw new CustomUserException("Already friends with this user");
        }

        if (friendRequestRepository.existsBySenderIdAndReceiverId(sender.getId(), receiverId)) {
            throw new CustomUserException("Friend request already sent");
        }

        var incomingRequest = friendRequestRepository.findBySenderIdAndReceiverId(receiverId, sender.getId());
        if (incomingRequest.isPresent()) {
            // The other user already sent a request, so accept it instead of leaving both users with a pending request to each other.
            createFriendship(sender, incomingRequest.get().getSender());
            friendRequestRepository.delete(incomingRequest.get());
            return;
        }

        friendRequestRepository.save(new FriendRequest(sender, receiver));
    }

    @Transactional
    public void acceptFriendRequest(Long requestId) {
        CustomUser currentUser = getAuthenticatedUser();

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomUserException("Friend request not found"));

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new CustomUserException("Not authorized to accept this request");
        }

        createFriendship(currentUser, request.getSender());
        friendRequestRepository.delete(request);
    }

    @Transactional
    public void declineFriendRequest(Long requestId) {
        CustomUser currentUser = getAuthenticatedUser();

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomUserException("Friend request not found"));

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new CustomUserException("Not authorized to decline this request");
        }

        friendRequestRepository.delete(request);
    }

    @Transactional
    public void removeFriend(Long friendId) {
        CustomUser currentUser = getAuthenticatedUser();
        userFriendRepository.deleteByUserIdAndFriendId(currentUser.getId(), friendId);
        userFriendRepository.deleteByUserIdAndFriendId(friendId, currentUser.getId());
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

    public List<FriendRequestDTO> getPendingRequests() {
        CustomUser currentUser = getAuthenticatedUser();

        return friendRequestRepository.findByReceiverIdOrderByCreatedAtDesc(currentUser.getId()).stream()
                .map(fr -> {
                    FriendRequestDTO dto = new FriendRequestDTO();
                    dto.id = fr.getId();
                    dto.senderId = fr.getSender().getId();
                    dto.senderUsername = fr.getSender().getUsername();
                    dto.createdAt = fr.getCreatedAt();
                    return dto;
                })
                .toList();
    }

    public FriendRequestCountDTO getPendingRequestCount() {
        CustomUser currentUser = getAuthenticatedUser();
        return new FriendRequestCountDTO(friendRequestRepository.countByReceiverId(currentUser.getId()));
    }

    public boolean areFriendsWith(Long userId, Long targetUserId) {
        return userFriendRepository.areFriends(userId, targetUserId);
    }

    private void createFriendship(CustomUser userA, CustomUser userB) {
        userFriendRepository.save(new UserFriend(userA, userB));
        userFriendRepository.save(new UserFriend(userB, userA));
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