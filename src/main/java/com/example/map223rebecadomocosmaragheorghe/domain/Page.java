package com.example.map223rebecadomocosmaragheorghe.domain;

import java.util.List;

public class Page {

    private String completeName;
    private List<Friendship> friends;
    private List<Chat> messages;
    private List<FriendRequest> requests;
    private List<Event> events;

    public Page(String completeName, List<Friendship> friends, List<Chat> messages, List<FriendRequest> requests, List<Event> events) {
        this.completeName = completeName;
        this.friends = friends;
        this.messages = messages;
        this.requests = requests;
        this.events = events;
    }

    public Page () {}

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public List<Friendship> getFriends() {
        return friends;
    }

    public void setFriendList(List<Friendship> friends) {
        this.friends = friends;
    }

    public List<Chat> getMessages() {
        return messages;
    }

    public void setMessages(List<Chat> messages) {
        this.messages = messages;
    }

    public List<FriendRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<FriendRequest> requests) {
        this.requests = requests;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Integer getNumberOfFriends() {
        return friends.size();
    }

    public Integer getNumberOfMessages() {
        return messages.size();
    }


    public Integer getNumberOfRequests() {
        return requests.size();
    }


    public Integer getNumberOfEvents() {
        return events.size();
    }
}
