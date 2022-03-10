package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;
import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Status;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.Validator;
import com.example.map223rebecadomocosmaragheorghe.repository.Repository;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryFriendRequest;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;
import com.example.map223rebecadomocosmaragheorghe.utils.events.ChangeEventType;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendRequestChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observable;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRequestService implements Observable<FriendRequestChangeEvent> {
    private RepositoryFriendRequest friendRequestRepository;
    private Repository<Tuple<Long>, Friendship> friendshipRepository;
    private Validator<FriendRequest> validator;
    PropertyChangeSupport propertyChangeSupport;
    private List<Observer<FriendRequestChangeEvent>> observers = new ArrayList<>();

    public FriendRequestService(RepositoryFriendRequest repositoryFriendRequest, Repository<Tuple<Long>, Friendship> friendshipRepository, Validator<FriendRequest> validator) {
        this.friendRequestRepository = repositoryFriendRequest;
        this.friendshipRepository = friendshipRepository;
        this.validator = validator;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Optional<FriendRequest> findRequest(Long requestID) {
        return friendRequestRepository.findOne(requestID);
    }

    public Optional<FriendRequest> addFriendRequest(Long from, Long to) {
        FriendRequest friendRequest = new FriendRequest(from, to, Status.pending, LocalDate.now());
        validator.validate(friendRequest);
        Optional<FriendRequest> fr = friendRequestRepository.save(friendRequest);
        if (fr.isEmpty())
            notifyObservers(new FriendRequestChangeEvent(ChangeEventType.ADD, friendRequest));
        else notifyObservers(new FriendRequestChangeEvent(ChangeEventType.ADD, null));
        propertyChangeSupport.firePropertyChange("request", null, null);
        return fr;
    }

    public Optional<FriendRequest> deleteFriendRequest(Long requestID) {
        Optional<FriendRequest> fr = friendRequestRepository.delete(requestID);
        if (fr.isPresent()) notifyObservers(new FriendRequestChangeEvent(ChangeEventType.DELETE, fr.get()));
        else notifyObservers(new FriendRequestChangeEvent(ChangeEventType.DELETE, null));
        propertyChangeSupport.firePropertyChange("request", null, null);
        return fr;
    }

    public Optional<FriendRequest> updateFriendRequest(Long requestID, Status status) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findOne(requestID);
        if (optionalFriendRequest.isPresent() && optionalFriendRequest.get().getStatus().equals(Status.pending)) {
            FriendRequest friendRequest = new FriendRequest(requestID, optionalFriendRequest.get().getFrom(), optionalFriendRequest.get().getTo(), status, LocalDate.now());
            validator.validate(friendRequest);
            if (status.equals(Status.approved))
                friendshipRepository.save(new Friendship(optionalFriendRequest.get().getFrom(), optionalFriendRequest.get().getTo(), LocalDate.now(), requestID));
            Optional<FriendRequest> fr = friendRequestRepository.update(friendRequest);
            if (fr.isEmpty()) notifyObservers(new FriendRequestChangeEvent(ChangeEventType.UPDATE, friendRequest));
            else notifyObservers(new FriendRequestChangeEvent(ChangeEventType.UPDATE, null));
            propertyChangeSupport.firePropertyChange("request", null, null);
            return fr;
        }
        return optionalFriendRequest;
    }

    public Iterable<FriendRequestDTO> getRequestsForOneUser(Long ID) {
        return friendRequestRepository.getRequestsForAUser(ID);
    }

    public List<FriendRequest> getRequestsForAUser(Long ID) {
        return friendRequestRepository.getRequestsForUser(ID);
    }

    public Iterable<FriendRequestDTO> getRequestsSentByOneUser(Long ID) {
        return friendRequestRepository.getRequestsSentByAUser(ID);
    }

    public List<FriendRequest> getRequestsByAUser(Long ID) {
        return friendRequestRepository.getRequestsByUser(ID);
    }

    public Optional<FriendRequest> existsRequest(Long user1, Long user2) {
        return friendRequestRepository.existRequest(user1, user2);
    }

    public boolean areFriends(Long user1, Long user2) {
        return  friendRequestRepository.areFriends(user1, user2);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void addObserver(Observer<FriendRequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendRequestChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendRequestChangeEvent t) {
        observers.forEach(o -> o.update(t));
    }
}