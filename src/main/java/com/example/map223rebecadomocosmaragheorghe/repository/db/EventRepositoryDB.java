package com.example.map223rebecadomocosmaragheorghe.repository.db;

import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.*;


public class EventRepositoryDB implements RepositoryEvent {

    private String url;
    private String username;
    private String password;

    public EventRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Event extractEvent(ResultSet resultSet) throws SQLException {
        Long IDEvent = resultSet.getLong("id_event");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        String photoUrl = resultSet.getString("photo_url");
        Long organizerID = resultSet.getLong("id_organizer");
        ArrayList<User> participants = makeParticipantsList(IDEvent);
        Event event = new Event(IDEvent, name, description, date, photoUrl, findUser(organizerID));
        event.setParticipants(participants);
        return event;
    }

    private User findUser(Long ID) {
        if(ID == null)
            throw new IllegalArgumentException("ID must be not null!");
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return extractUser(resultSet);
        } catch (SQLException ignored){}
        return null;
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        Long ID = resultSet.getLong("id");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String password = resultSet.getString("password");
        String photoUrl = resultSet.getString("photo_url");
        return new User(ID, email, firstName, lastName, password, photoUrl);
    }

    private ArrayList<User> makeParticipantsList (Long eventID) {
        ArrayList<User> participants = new ArrayList<>();
        getParticipantsForAnEvent(eventID).forEach(participants::add);
        return participants;
    }

    @Override
    public Optional<Event> findOne(Long eventID) {
        if(eventID == null )
            throw new IllegalArgumentException("ID must not be null!");
        String sql ="SELECT * FROM events WHERE id_event = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return Optional.of(extractEvent(resultSet));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        String sql = "SELECT * FROM events";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while(resultSet.next())
                events.add(extractEvent(resultSet));
        } catch (SQLException ignored) {}
        return events;
    }

    @Override
    public Optional<Event> save(Event event) {
        String sql = "INSERT INTO events (name, description, date, photo_url, id_organizer) values ( ?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, event.getName());
            preparedStatement.setString(2, event.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(event.getDate()));
            preparedStatement.setString(4, event.getPhotoUrl());
            preparedStatement.setLong(5, event.getOrganizer().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: insert or update on table \"events\" violates foreign key constraint"))
                throw new ValidationException("The user and the event must exist!");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Event> delete(Long eventID) {
        Optional<Event> optionalEvent = findOne(eventID);
        if(optionalEvent.isEmpty())
            return optionalEvent;
        String sql = "DELETE FROM events WHERE id_event = ? ";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return optionalEvent;
    }

    @Override
    public Optional<Event> update(Event event) {
        Optional<Event> optionalEvent = findOne(event.getId());
        if(optionalEvent.isEmpty())
            return Optional.of(event);
        String sql = "UPDATE events SET date = ?, name = ?, description =?, photo_url = ? WHERE id_event = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(event.getDate()));
            preparedStatement.setString(2, event.getName());
            preparedStatement.setString(3, event.getDescription());
            preparedStatement.setString(4, event.getPhotoUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<Event> getEventsForAUser(Long userID) {
        Set<Event> events = new HashSet<>();
        String sql = "select e.id_event, e.name, e.description, e.date, e.photo_url, e.id_organizer from event_users as eu " +
        "inner join events as e on (eu.id_event = e.id_event and eu.id_user = ? and eu.notifications = true) where e.date >= ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                events.add(extractEvent(resultSet));
        } catch (SQLException ignored) {ignored.printStackTrace();}
        return events;
    }

    @Override
    public Iterable<User> getParticipantsForAnEvent(Long eventID){
        Set<User> participants = new HashSet<>();
        String sql = "select u.id, u.first_name, u.last_name, u.email, u.password, u.photo_url from event_users as eu " +
                "inner join users as u on (eu.id_user = u.id and eu.id_event = ?) ";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                participants.add(extractUser(resultSet));
            }
        } catch (SQLException ignored) {}
        return participants;
    }

    @Override
    public Iterable<Event> getSomeEvents(int offset, int limit) {
        Set<Event> events = new HashSet<>();
        String sql = "SELECT * FROM events WHERE date >= ? offset ? limit ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setLong(2, offset);
            preparedStatement.setLong(3, limit);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                events.add(extractEvent(resultSet));
        } catch (SQLException ignored) {}
        return events;
    }

    @Override
    public List<Event> getEventsMadeByAnUser(Long userID) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE id_organizer = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                events.add(extractEvent(resultSet));
        } catch (SQLException ignored) {}
        return events;
    }

    @Override
    public void changeNotificationsStatus(Long userID, Long eventID, boolean bool) {
        String sql = "UPDATE event_users SET notifications = ? WHERE id_event = ? and id_user = ? ";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, bool);
            preparedStatement.setLong(2, eventID);
            preparedStatement.setLong(3, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
    }

    @Override
    public boolean getNotificationStatus(Long userID, Long eventID) {
        String sql = "SELECT notifications FROM event_users WHERE id_event = ? and id_user = ? ";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            preparedStatement.setLong(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return resultSet.getBoolean(1);
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public int noOfEvents() {
        int no = 0;
        String sql = "SELECT COUNT(*) FROM events WHERE date >= ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                no = resultSet.getInt(1);
        } catch (SQLException ignored) {}
        return no;
    }

    @Override
    public void saveParticipantToAnEvent(Long userID, Long eventID) {
        String sql = "INSERT INTO event_users (id_event, id_user) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            preparedStatement.setLong(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
    }

    @Override
    public void deleteParticipantToAnEvent(Long userID, Long eventID) {
        String sql = "DELETE FROM event_users WHERE id_event = ? and id_user = ? ";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, eventID);
            preparedStatement.setLong(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
    }
}