package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;

import java.util.Optional;
import java.util.List;

public interface RepositoryFriendRequest extends Repository<Long, FriendRequest> {

    /**Gets friend requests for a user.
     * @param to the ID of the user
     * @return the users that sent a request
     */;

    Iterable<FriendRequestDTO> getRequestsForAUser(Long to);

    List<FriendRequest> getRequestsForUser(Long to);

    Iterable<FriendRequestDTO> getRequestsSentByAUser(Long from);

    List<FriendRequest> getRequestsByUser(Long from);

    Optional<FriendRequest> existRequest(Long user1, Long user2);

    boolean areFriends(Long user1, Long user2);
}
