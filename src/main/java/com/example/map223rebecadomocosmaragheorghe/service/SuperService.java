package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.MessageDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SuperService implements PropertyChangeListener {
    public UserService userService;
    public FriendshipService friendshipService;
    public FriendRequestService friendRequestService;
    public MessageService messageService;
    public CommunityService communityService;
    public ChatService chatService;
    public EventService eventService;
    public User user;
    public Page page = new Page();
    @FXML
    public AnchorPane mainPane;
    public PropertyChangeSupport propertyChangeSupport;

    public SuperService(UserService userService,
                        FriendshipService friendshipService,
                        FriendRequestService friendRequestService,
                        MessageService messageService,
                        CommunityService communityService,
                        ChatService chatService, EventService eventService,
                        User user) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendRequestService = friendRequestService;
        this.messageService = messageService;
        this.communityService = communityService;
        this.chatService = chatService;
        this.eventService = eventService;
        this.user = user;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.eventService.addPropertyChangeListener(this);
        this.friendRequestService.addPropertyChangeListener(this);
        this.chatService.addPropertyChangeListener(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Page getPage() {
        return page;
    }

    public void setPage() {
        this.page.setCompleteName(user.getCompleteName());
        this.page.setFriendList(friendshipService.getAllFriendsForAUser(user.getId()));
        setPageMessages();
        setPageEvents();
        setPageRequests();
    }

    public void setPageRequests() {
        List<FriendRequest> listRequests = friendRequestService.getRequestsForAUser(user.getId());
        List<FriendRequest> requests = new ArrayList<>();
        listRequests.forEach(f -> {
            if(f.getStatus().equals(Status.pending))
                requests.add(f);
        });
        this.page.setRequests(requests);
    }

    public void setPageEvents() {
        Iterable<Event> iterableEvents = eventService.getEventsForAUser(user.getId());
        List<Event> events = new ArrayList<>();
        iterableEvents.forEach(events::add);
        this.page.setEvents(events.stream()
                .filter(event -> event.getDate().isAfter(LocalDateTime.now()))
                .toList());
    }

    public void setPageMessages() {
        Iterable<Chat> iterableChats = chatService.getChatsForOne(user.getId());
        List<Chat> chats = new ArrayList<>();
        iterableChats.forEach(chats::add);
        this.page.setMessages(chats);
    }

    public void setMainPane(AnchorPane pane) {
        this.mainPane = pane;
    }

    public List<MessageDTO> getAllMessagesReport(LocalDateTime startDate, LocalDateTime endDate) {
        var chatsOfUser = StreamSupport.stream(chatService.getChatsForOne(user.getId()).spliterator(), false)
                .map(Entity::getId).collect(Collectors.toList());
        var messagesOfUser = StreamSupport.stream(messageService.findAllMessages().spliterator(), false)
                .filter(message -> chatsOfUser.contains(message.getIdChat()) && message.getDate().isAfter(startDate) && message.getDate().isBefore(endDate))
                .collect(Collectors.toList());
        return messagesOfUser.stream()
                .map(message -> new MessageDTO(message.getBody(),message.getFrom(), message.getDate(), message.getIdChat()))
                .collect(Collectors.toList());

    }

    public List<UserDTO> getAllNewFriendsReport(LocalDateTime startDate, LocalDateTime endDate) {
        var newFriendships = friendshipService.getAllFriendsForAUser(user.getId()).stream()
                .filter(friendship -> friendship.getDate().atStartOfDay().isAfter(startDate)
                && friendship.getDate().atStartOfDay().isBefore(endDate))
                .collect(Collectors.toList());
        return newFriendships.stream()
                .map(friendship -> {
                    var request = friendRequestService.findRequest(friendship.getRequestID()).get();
                    var user1 = request.getFrom();
                    var user2 = request.getTo();
                    User friend;
                    if (user1.equals(this.user.getId())) {
                        friend = userService.findOneUser(user2).get();
                    }
                    else friend = userService.findOneUser(user1).get();
                    return new UserDTO(friend.getFirstName(), friend.getLastName(), friend.getEmail(), friend.getPhotoUrl());
                })
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesWithAUserReport(UserDTO friend, LocalDateTime startDate, LocalDateTime endDate) {
        var chatsOfUser = StreamSupport.stream(chatService.getChatsForOne(user.getId()).spliterator(), false)
                .filter(chat -> chat.getParticipants().contains(userService.findOneByEmail(friend.getEmail()).get()) && chat.getParticipants().size() == 2)
                .map(Entity::getId).collect(Collectors.toList());
        var messagesOfUser = StreamSupport.stream(messageService.findAllMessages().spliterator(), false)
                .filter(message -> chatsOfUser.contains(message.getIdChat()) && message.getDate().isAfter(startDate) && message.getDate().isBefore(endDate))
                .filter(message -> message.getFrom().getEmail().equals(friend.getEmail()))
                .collect(Collectors.toList());
        return messagesOfUser.stream()
                .map(message -> new MessageDTO(message.getBody(),message.getFrom(), message.getDate(), message.getIdChat()))
                .collect(Collectors.toList());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("event") && user != null) {
            setPageEvents();
        }
        else if(evt.getPropertyName().equals("request") && user != null) {
            setPageRequests();
        }
        else if (evt.getPropertyName().equals("chat") && user != null){
            setPageMessages();
        }
        propertyChangeSupport.firePropertyChange(null, null, null);
    }
}
