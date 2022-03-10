package com.example.map223rebecadomocosmaragheorghe.service.DTO;

import com.example.map223rebecadomocosmaragheorghe.domain.User;

import java.util.List;

public class ChatDTO {
    Long id;
    String name;
    List<User> participants;
    String picture;
    MessageDTO lastMessage;

    public ChatDTO(String name, List<User> participants, String picture) {
        this.name = name;
        this.participants = participants;
        this.picture = picture;
    }

    public ChatDTO(Long id, String name, List<User> participants, String picture, MessageDTO lastMessage) {
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.picture = picture;
        this.lastMessage = lastMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageDTO getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDTO lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public String getPicture() {return picture;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
                "name='" + name + ' ' +
                ", picture='" + picture + ' ' +
                '}';
    }
}
