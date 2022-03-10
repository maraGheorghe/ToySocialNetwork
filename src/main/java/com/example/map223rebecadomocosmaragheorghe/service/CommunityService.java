package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.Community;
import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.repository.Repository;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendshipDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;

import java.util.*;
import java.util.function.Predicate;

public class CommunityService {

    private Repository<Long, User> userRepository;
    private Repository<Tuple<Long>, Friendship> friendshipRepository;

    /**Constructor of the class.
     * @param userRepository Repository representing the repository for users.
     */

    public CommunityService(Repository<Long, User> userRepository, Repository<Tuple<Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public List<FriendshipDTO> getAllFriendshipsOfAUser(Long userID, Predicate<Friendship> predicate) {
        List<FriendshipDTO> friendsOfUser = new ArrayList<>();
        Optional<User> user = userRepository.findOne(userID);
        if (user.isEmpty())
            throw new ValidationException("User does not exist.");
        List<Friendship> allFriendships = new ArrayList<>();
        friendshipRepository.findAll().forEach(allFriendships::add);
        allFriendships.stream().filter(predicate).forEach(friendship -> {
            Optional<User> optionalUser;
            if (friendship.getId().getRight().equals(userID))
                optionalUser = userRepository.findOne(friendship.getId().getLeft());
            else
                optionalUser = userRepository.findOne(friendship.getId().getRight());
            if(optionalUser.isPresent()) {
                UserDTO friendDto = new UserDTO(optionalUser.get().getFirstName(), optionalUser.get().getLastName(), optionalUser.get().getEmail(), optionalUser.get().getPhotoUrl());
                FriendshipDTO friendshipDto = new FriendshipDTO(friendDto, friendship.getDate());
                friendsOfUser.add(friendshipDto);
            }
        });
        return friendsOfUser;
    }

    public Predicate<Friendship> getFriendsOfAUser(Long userID) {
        return  x -> (x.getId().getLeft().equals(userID) || x.getId().getRight().equals(userID));
    }

    public Predicate<Friendship> getFriendsOfAUser(Long userID, String month) {
        return  x -> ((x.getId().getLeft().equals(userID) || x.getId().getRight().equals(userID)) && x.getDate().getMonth().toString().equals(month));
    }

    /**Makes the adjacency list.
     * @param adjMapOfUsers the adjacency list.
     */
    private void makeAdjMapOfUsers(HashMap<User, List<User>> adjMapOfUsers){
        userRepository.findAll().forEach(user -> {
            adjMapOfUsers.put(user, user.getFriendsList());
        });
    }

    /**Finds a community.
     * @param user a User representing the first user from a community.
     * @param visited a HashMap representing which user has been visited.
     * @param community a List of user that are part of the community.
     * @param adjMapOfUsers the adjacency list.
     */
    private void DFSUtil(User user, HashMap<User, Boolean> visited, ArrayList<User> community, HashMap<User, List<User>> adjMapOfUsers){
        visited.put(user, true);
        community.add(user);
        List<User> friendList = adjMapOfUsers.get(user);
        friendList.forEach(friend -> {
            if(!visited.containsKey(friend))
                DFSUtil(friend, visited, community, adjMapOfUsers);
        });
    }

    /**Finds all the communities.
     * @param communities representing a List of all the communities
     */
    private void DFS(ArrayList<Community> communities){
        HashMap<User, List<User>> adjMapOfUsers = new HashMap<>();
        makeAdjMapOfUsers(adjMapOfUsers);
        HashMap<User, Boolean> visited = new HashMap<>();
        userRepository.findAll().forEach(user -> {
            ArrayList<User> community = new ArrayList<>();
            if(!visited.containsKey(user)){
                DFSUtil(user, visited, community, adjMapOfUsers);
                communities.add(new Community(community));
            }
        });
    }

    /**Gets the number of communities.
     * @return an int representing the number of community=ies.
     */
    public int getNumberOfCommunities(){
        ArrayList<Community> communities = new ArrayList<>();
        DFS(communities);
        return communities.size();
    }

    /**Gets the most sociable community.
     * @return a list of users-dto containing the users from the most sociable community.
     */
    public List<User> getMostSociableCommunity(){
        ArrayList<Community> communities = new ArrayList<>();
        DFS(communities);
        Community mostSociable = communities.get(0);
        Integer maxim = communities.get(0).getUsers().size();
        for(Community community: communities)
            if(community.getUsers().size() > maxim){
                maxim = community.getUsers().size();
                mostSociable = community;
            }
        return mostSociable.getUsers();
    }
}
