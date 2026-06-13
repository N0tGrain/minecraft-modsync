package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);
    long countByReceiverId(Long receiverId);
}
