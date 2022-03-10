package com.example.map223rebecadomocosmaragheorghe.utils.events;

public class EventChangeEvent implements Event {
    private ChangeEventType type;
    private com.example.map223rebecadomocosmaragheorghe.domain.Event data, oldData;

    public EventChangeEvent(ChangeEventType type, com.example.map223rebecadomocosmaragheorghe.domain.Event data) {
        this.type = type;
        this.data = data;
    }

    public EventChangeEvent(ChangeEventType type, com.example.map223rebecadomocosmaragheorghe.domain.Event data, com.example.map223rebecadomocosmaragheorghe.domain.Event oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return this.type;
    }

    public com.example.map223rebecadomocosmaragheorghe.domain.Event getData() {
        return data;
    }

    public com.example.map223rebecadomocosmaragheorghe.domain.Event getOldData() {
        return oldData;
    }
}