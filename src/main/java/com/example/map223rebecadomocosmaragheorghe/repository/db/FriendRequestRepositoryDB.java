package com.example.map223rebecadomocosmaragheorghe.repository.db;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;
import com.example.map223rebecadomocosmaragheorghe.domain.Status;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryFriendRequest;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;

public class FriendRequestRepositoryDB implements RepositoryFriendRequest {
    private String url;
    private String username;
    private String password;

    public FriendRequestRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private FriendRequest extractFriendRequest(ResultSet resultSet) throws SQLException {
        Long requestID = resultSet.getLong("request_id");
        Long from = resultSet.getLong("from_user");
        Long to = resultSet.getLong("to_user");
        Status status = Status.valueOf(resultSet.getString("status"));
        LocalDate date = resultSet.getDate("date").toLocalDate();
        return new FriendRequest(requestID, from, to, status, date);
    }

    private List<FriendRequest> findRequests(Long firstUser, Long secondUser) {
        List<FriendRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM friend_request WHERE (to_user = ? and from_user = ?) or (to_user = ? and from_user = ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, firstUser);
            preparedStatement.setLong(2, secondUser);
            preparedStatement.setLong(3, secondUser);
            preparedStatement.setLong(4, firstUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                requests.add(extractFriendRequest(resultSet));
            return requests;
        } catch (SQLException ignored){}
        return requests;
    }

    @Override
    public Optional<FriendRequest> findOne(Long requestID) {
        if(requestID == null)
            throw new IllegalArgumentException("ID must not be null!");
        String sql ="SELECT * FROM friend_request WHERE request_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, requestID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return Optional.of(extractFriendRequest(resultSet));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> requests = new HashSet<>();
        String sql = "SELECT * FROM friend_request";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while(resultSet.next())
                requests.add(extractFriendRequest(resultSet));
        } catch (SQLException ignored) {}
        return requests;
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest friendRequest) {
        List<FriendRequest> requests = findRequests(friendRequest.getFrom(), friendRequest.getTo());
        if (requests.size() != 0)
            requests.removeIf(request ->
                    request.getStatus().equals(Status.rejected)
            );
        if(requests.size() != 0)
            return Optional.of(requests.get(0));
        String sql = "INSERT INTO friend_request (from_user, to_user, status, date) values (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, friendRequest.getFrom());
            preparedStatement.setLong(2, friendRequest.getTo());
            preparedStatement.setString(3, friendRequest.getStatus().toString());
            preparedStatement.setDate(4, Date.valueOf(friendRequest.getDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getMessage().contains("ERROR: insert or update on table \"friendships\" violates foreign key constraint"))
                throw new ValidationException("The users must exist!");
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> delete(Long requestID) {
        Optional<FriendRequest> optionalFriendRequest = findOne(requestID);
        if(optionalFriendRequest.isEmpty())
            return optionalFriendRequest;
        String sql = "DELETE FROM friend_request WHERE request_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, requestID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return optionalFriendRequest;
    }

    @Override
    public Optional<FriendRequest> update(FriendRequest friendRequest) {
        Optional<FriendRequest> optionalFriendRequest = findOne(friendRequest.getId());
        if(optionalFriendRequest.isEmpty())
            return Optional.of(friendRequest);
        String sql = "UPDATE friend_request SET status = ? WHERE request_id = ?; ";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, friendRequest.getStatus().toString());
            preparedStatement.setLong(2, friendRequest.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    private FriendRequestDTO extractFriendRequestDTO(ResultSet resultSet) throws SQLException {
        Long requestID = resultSet.getLong("request_id");
        String status = resultSet.getString("status");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        String email = resultSet.getString("email");
        String firstname = resultSet.getString("first_name");
        String lastname = resultSet.getString("last_name");
        String photoUrl = resultSet.getString("photo_url");
        return new FriendRequestDTO(requestID, email, firstname, lastname, photoUrl, status, date);
    }

    @Override
    public Iterable<FriendRequestDTO> getRequestsForAUser(Long to) {
        Set<FriendRequestDTO> requests = new HashSet<>();
        String sql = "SELECT fr.request_id, fr.status, fr.date, u.email, u.first_name, u.last_name, u.photo_url FROM friend_request fr " +
                "INNER JOIN users u ON fr.from_user = u.id WHERE to_user = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, to);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                requests.add(extractFriendRequestDTO(resultSet));
        } catch (SQLException ignored) {}
        return requests;
    }

    @Override
    public List<FriendRequest> getRequestsForUser(Long to) {
        List<FriendRequest> requests = new ArrayList<>();
        String sql = "SELECT fr.request_id, fr.from_user, fr.to_user, fr.status, fr.date, u.email, u.first_name, u.last_name, u.photo_url FROM friend_request fr " +
                "INNER JOIN users u ON fr.from_user = u.id WHERE to_user = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, to);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                requests.add(extractFriendRequest(resultSet));
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return requests;
    }

    @Override
    public Iterable<FriendRequestDTO> getRequestsSentByAUser(Long from) {
        Set<FriendRequestDTO> requests = new HashSet<>();
        String sql = "SELECT fr.request_id, fr.status, fr.date, u.email, u.first_name, u.last_name, u.photo_url FROM friend_request fr " +
                "INNER JOIN users u ON fr.to_user = u.id WHERE from_user = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, from);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                requests.add(extractFriendRequestDTO(resultSet));
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<FriendRequest> getRequestsByUser(Long from) {
        List<FriendRequest> requests = new ArrayList<>();
        String sql = "SELECT fr.request_id, fr.status, fr.from_user, fr.to_user fr.date, u.email, u.first_name, u.last_name, u.photo_url FROM friend_request fr " +
                "INNER JOIN users u ON fr.to_user = u.id WHERE from_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, from);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                requests.add(extractFriendRequest(resultSet));
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return requests;
    }

    @Override
    public Optional<FriendRequest> existRequest(Long user1, Long user2) {
        String sql ="SELECT * FROM friend_request WHERE ((to_user = ? and from_user = ?) " +
                "or (to_user = ? and from_user = ?)) and (status = 'pending' or status = 'approved')";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user1);
            preparedStatement.setLong(2, user2);
            preparedStatement.setLong(3, user2);
            preparedStatement.setLong(4, user1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return Optional.of(extractFriendRequest(resultSet));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public boolean areFriends(Long user1, Long user2) {
        String sql ="SELECT * FROM friend_request WHERE ((to_user = ? and from_user = ?) " +
                "or (to_user = ? and from_user = ?)) and (status = 'approved')";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user1);
            preparedStatement.setLong(2, user2);
            preparedStatement.setLong(3, user2);
            preparedStatement.setLong(4, user1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return true;
        } catch (SQLException ignored) {}
        return false;
    }

}