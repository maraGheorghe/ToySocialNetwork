package com.example.map223rebecadomocosmaragheorghe.utils.events;


import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;

public class MessageChangeEvent implements  Event{
    private ChangeEventType type;
    private Chat data, oldData;

    public MessageChangeEvent(ChangeEventType type, Chat data) {
        this.type = type;
        this.data = data;
    }

    public MessageChangeEvent(ChangeEventType type, Chat data, Chat oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return this.type;
    }

    public Chat getData() {
        return data;
    }

    public Chat getOldData() {
        return oldData;
    }
}
