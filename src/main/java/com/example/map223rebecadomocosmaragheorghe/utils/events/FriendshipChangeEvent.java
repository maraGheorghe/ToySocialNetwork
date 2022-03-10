package com.example.map223rebecadomocosmaragheorghe.utils.events;


import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;

public class FriendshipChangeEvent implements  Event{
    private ChangeEventType type;
    private Friendship data, oldData;

    public FriendshipChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public FriendshipChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return this.type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}

