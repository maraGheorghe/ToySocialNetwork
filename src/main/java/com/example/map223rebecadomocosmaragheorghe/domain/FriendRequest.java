package com.example.map223rebecadomocosmaragheorghe.domain;

import java.time.LocalDate;

public class FriendRequest extends Entity<Long>{
    Long from;
    Long to;
    Status status;
    LocalDate date;

    public FriendRequest(Long from, Long to, Status status, LocalDate date){
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public FriendRequest(Long requestID, Long from, Long to, Status status, LocalDate date){
        setId(requestID);
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.from + " send a friend request to " + this.to
                + " (status: " + this.status + ", ID: " + this.getId() + ")";
    }
}
