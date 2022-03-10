package com.example.map223rebecadomocosmaragheorghe.utils.observer;

import com.example.map223rebecadomocosmaragheorghe.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
