package com.example.map223rebecadomocosmaragheorghe;

import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.FriendRequestValidator;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.FriendshipValidator;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.MessageValidator;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.UserValidator;
import com.example.map223rebecadomocosmaragheorghe.repository.*;
import com.example.map223rebecadomocosmaragheorghe.repository.db.FriendRequestRepositoryDB;
import com.example.map223rebecadomocosmaragheorghe.repository.db.FriendshipRepositoryDB;
import com.example.map223rebecadomocosmaragheorghe.repository.db.MessageRepositoryDB;
import com.example.map223rebecadomocosmaragheorghe.repository.db.UserRepositoryDB;
import com.example.map223rebecadomocosmaragheorghe.service.*;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;

public class Main {

    public static void main(String[] args) {
        RepositoryUser userRepository = new UserRepositoryDB(Constants.url, Constants.username, Constants.password);
        RepositoryFriendship friendshipRepository = new FriendshipRepositoryDB(Constants.url, Constants.username, Constants.password);
        RepositoryMessage messageRepository = new MessageRepositoryDB(Constants.url, Constants.username, Constants.password);
        RepositoryFriendRequest friendRequestRepository = new FriendRequestRepositoryDB(Constants.url, Constants.username, Constants.password);
        UserService userService = new UserService(userRepository, new UserValidator());
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, new FriendshipValidator());
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository, friendshipRepository, new FriendRequestValidator());
        CommunityService communityService = new CommunityService(userRepository,friendshipRepository);
        MessageService messageService = new MessageService(messageRepository, new MessageValidator());
    }
}
