package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.User;


import java.util.List;

public interface RepositoryEvent extends Repository<Long, Event>{

    Iterable<Event> getEventsForAUser(Long userID);

    Iterable<User> getParticipantsForAnEvent(Long userID);

    Iterable<Event> getSomeEvents(int offset, int limit);

    List <Event> getEventsMadeByAnUser(Long userID);

    void changeNotificationsStatus(Long userID, Long eventID, boolean bool);

    boolean getNotificationStatus(Long userID, Long eventID);

    int noOfEvents();

    void saveParticipantToAnEvent(Long userID, Long eventID);

    void deleteParticipantToAnEvent(Long userID, Long eventID);
}
