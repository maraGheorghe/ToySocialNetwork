package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;
import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.Validator;
import com.example.map223rebecadomocosmaragheorghe.repository.Repository;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryFriendship;
import com.example.map223rebecadomocosmaragheorghe.utils.events.ChangeEventType;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendRequestChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendshipChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observable;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipService implements Observable<FriendshipChangeEvent> {

    private RepositoryFriendship friendshipRepository;
    Validator<Friendship> validator;
    private List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();

    /**Class constructor.
     * @param friendshipRepository Repository representing the repository for friendships.
     */
    public FriendshipService(RepositoryFriendship friendshipRepository, Validator<Friendship> validator) {
        this.friendshipRepository = friendshipRepository;
        this.validator = validator;
    }

    /**Adds a friendship.
     * @param idFirstFriend a Long representing the ID of the first friend.
     * @param idSecondFriend a Long representing the ID of the second friend.
     * @return an {@code Optional} encapsulating the friendship if that already exist, null otherwise.
     */
    public Optional<Friendship> addFriendship(Long idFirstFriend, Long idSecondFriend){
        Friendship friendship = new Friendship(idFirstFriend, idSecondFriend, LocalDate.now(), 0L);
        validator.validate(friendship);
        return friendshipRepository.save(friendship);
    }

    /**Removes a friendship.
     * @param idFirstFriend a Long representing the ID of the first friend.
     * @param idSecondFriend a Long representing the ID of the second friend.
     * @return an {@code Optional} encapsulating the friendship that has been deleted or null if the friendship does not exist.
     */
    public Optional<Friendship> deleteFriendship(Long idFirstFriend, Long idSecondFriend){
        Optional<Friendship> optionalFriendship = friendshipRepository.delete(new Tuple<>(idFirstFriend, idSecondFriend));
        if (optionalFriendship.isPresent()) notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, optionalFriendship.get()));
        else notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, null));
        return optionalFriendship;
    }

    /**Updates a friendship.
     * @param idFirstFriend a Long representing the ID of the first friend.
     * @param idSecondFriend a Long representing the ID of the second friend.
     * @return an {@code Optional} encapsulating the friendship if that doesn't already exist, null otherwise.
     */
    public Optional<Friendship> updateFriendship(Long idFirstFriend, Long idSecondFriend, LocalDate date){
        Friendship friendship = new Friendship(idFirstFriend, idSecondFriend, date, 0L);
        validator.validate(friendship);
        return friendshipRepository.update(friendship);
    }

    /**Finds a friendship.
     * @param idFirstFriend a Long representing the ID of the first friend.
     * @param idSecondFriend a Long representing the ID of the second friend.
     * @return an {@code Optional} encapsulating the friendship that has the two emails or null if it does not exist.
     */
    public Optional<Friendship> findOneFriendship(Long idFirstFriend, Long idSecondFriend){
        return friendshipRepository.findOne(new Tuple<>(idFirstFriend, idSecondFriend));
    }

    /**Finds all the friendships that are registered.
     * @return an Iterable of friendships representing all the friendships.
     */
    public Iterable<Friendship> findAllFriendships(){
        return friendshipRepository.findAll();
    }

    public List<Friendship> getAllFriendsForAUser(Long userID) {
        return friendshipRepository.getAllFriendshipsForAUser(userID);
    }


    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.forEach(o -> o.update(t));
    }

}

