package com.example.map223rebecadomocosmaragheorghe.utils.observer;

import com.example.map223rebecadomocosmaragheorghe.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
