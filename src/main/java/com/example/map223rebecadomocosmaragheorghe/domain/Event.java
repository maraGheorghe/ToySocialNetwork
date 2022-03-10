package com.example.map223rebecadomocosmaragheorghe.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long> {
    private String name;
    private String description;
    private LocalDateTime date;
    private String photoUrl;
    private User organizer;
    public List<User> participants;

    public Event(String name, String description, LocalDateTime date, String photoUrl, User organizer) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.photoUrl = photoUrl;
        this.organizer = organizer;
        participants = new ArrayList<>();
    }

    public Event(Long ID, String name, String description, LocalDateTime date, String photoUrl,  User organizer) {
        setId(ID);
        this.name = name;
        this.description = description;
        this.date = date;
        this.photoUrl = photoUrl;
        this.organizer = organizer;
        participants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", photoUrl='" + photoUrl + '\'' +
                ", organizer=" + organizer +
                ", participants=" + participants +
                '}';
    }
}
