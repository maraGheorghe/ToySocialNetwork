package com.example.map223rebecadomocosmaragheorghe.service;
import com.example.map223rebecadomocosmaragheorghe.domain.*;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.Validator;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.events.ChangeEventType;
import com.example.map223rebecadomocosmaragheorghe.utils.events.EventChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observable;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventService implements Observable<EventChangeEvent> {

    RepositoryEvent eventRepository;
    Validator<Event> validator;
    PropertyChangeSupport propertyChangeSupport;
    private List<Observer<EventChangeEvent>> observers = new ArrayList<>();

    public EventService(RepositoryEvent eventRepository, Validator<Event> validator) {
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Optional<Event> findEvent(Long eventID) {
        return eventRepository.findOne(eventID);
    }

    public Iterable<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> addEvent(String name, String description, LocalDateTime date, String photoUrl, User organizer) {
        Event event = new Event(name, description, date, photoUrl, organizer);
        validator.validate(event);
        Optional<Event> optionalEvent = eventRepository.save(event);
        if (optionalEvent.isEmpty()) notifyObservers(new EventChangeEvent(ChangeEventType.ADD, event));
        else notifyObservers(new EventChangeEvent(ChangeEventType.ADD, null));
        return optionalEvent;
    }

    public Optional<Event> deleteEvent(Long eventID) {
        Optional<Event> optionalEvent = eventRepository.delete(eventID);
        if (optionalEvent.isPresent()) notifyObservers(new EventChangeEvent(ChangeEventType.DELETE, optionalEvent.get()));
        else notifyObservers(new EventChangeEvent(ChangeEventType.DELETE, null));
        return optionalEvent;
    }

    public Optional<Event> updateEvent(Long eventID, String name, String description, LocalDateTime date, String photoUrl, User organizer) {
        Event event = new Event(eventID, name, description, date, photoUrl, organizer);
        validator.validate(event);
        Optional<Event> optionalEvent = eventRepository.update(event);
        if (optionalEvent.isEmpty()) notifyObservers(new EventChangeEvent(ChangeEventType.UPDATE, event));
        else notifyObservers(new EventChangeEvent(ChangeEventType.UPDATE, null));
        return optionalEvent;
    }

    public Iterable<Event> getEventsForAUser(Long userID) {
        return eventRepository.getEventsForAUser(userID);
    }

    public Iterable<User> getParticipantsForAnEvent (Long eventID) {
        return eventRepository.getParticipantsForAnEvent(eventID);
    }

    public void addParticipant(Long userID, Long eventID) {
        eventRepository.saveParticipantToAnEvent(userID, eventID);
        notifyObservers(new EventChangeEvent(ChangeEventType.UPDATE, null));
        propertyChangeSupport.firePropertyChange("event", null, null);
    }

    public void cancelParticipation(Long userID, Long eventID) {
        eventRepository.deleteParticipantToAnEvent(userID, eventID);
        notifyObservers(new EventChangeEvent(ChangeEventType.UPDATE, null));
        propertyChangeSupport.firePropertyChange("event", null, null);
    }

    public Iterable<Event> getSomeEvents(int offset, int limit) {
        return eventRepository.getSomeEvents(offset, limit);
    }

    public List<Event> getEventsMadeByAnUser(Long userID) {
        return eventRepository.getEventsMadeByAnUser(userID);
    }

    public int getNoOfEvents() {
        return eventRepository.noOfEvents();
    }

    public void changeNotificationsStatus(Long userID, Long eventID, boolean bool) {
        eventRepository.changeNotificationsStatus(userID, eventID, bool);
        propertyChangeSupport.firePropertyChange("event", null, null);
    }

    public boolean getNotificationStatus(Long userID, Long eventID) {
        return eventRepository.getNotificationStatus(userID, eventID);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void addObserver(Observer<EventChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EventChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EventChangeEvent t) {
        observers.forEach(o -> o.update(t));
    }
}