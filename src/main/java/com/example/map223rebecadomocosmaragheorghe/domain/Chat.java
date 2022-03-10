package com.example.map223rebecadomocosmaragheorghe.domain;

import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;

import java.util.List;

public class Chat extends Entity<Long> {
    private String name;
    private List<User> participants;
    private String picture;

    public Chat(String name, List<User> participants, String picture) {
        this.name = name;
        this.participants = participants;
        this.picture = picture;
    }

    public Chat(ChatDTO chatDTO) {
        this.name = chatDTO.getName();
        this.participants = chatDTO.getParticipants();
        this.picture = chatDTO.getPicture();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public String getPicture() {return picture;}

    public void setPicture(String picture) {this.picture = picture;}

    @Override
    public String toString() {
        return "Chat{" +
                "name='" + name + '\'' +
                ", participants=" + participants +
                ", picture='" + picture + '\'' +
                '}';
    }
}
