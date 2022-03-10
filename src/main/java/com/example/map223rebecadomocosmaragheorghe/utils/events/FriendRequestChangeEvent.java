package com.example.map223rebecadomocosmaragheorghe.utils.events;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;

public class FriendRequestChangeEvent implements Event {
    private ChangeEventType type;
    private FriendRequest data, oldData;

    public FriendRequestChangeEvent(ChangeEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }

    public FriendRequestChangeEvent(ChangeEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return this.type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
