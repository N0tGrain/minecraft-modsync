package com.n0tgrain.modsyncbackend.repositories;

import com.n0tgrain.modsyncbackend.models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
    Optional<UserFriend> findByUserIdAndFriendId(Long userId, Long friendId);

    List<UserFriend> findByUserId(Long userId);

    @Query("SELECT COUNT(uf) > 0 FROM UserFriend uf WHERE (uf.user.id = :userId AND uf.friend.id = :friendId) OR (uf.user.id = :friendId AND uf.friend.id = :userId)")
    boolean areFriends(@Param("userId") Long userId, @Param("friendId") Long friendId);

    void deleteByUserIdAndFriendId(Long userId, Long friendId);
}
