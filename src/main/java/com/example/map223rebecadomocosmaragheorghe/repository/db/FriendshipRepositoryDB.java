package com.example.map223rebecadomocosmaragheorghe.repository.db;

import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.repository.Repository;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryFriendship;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class FriendshipRepositoryDB implements RepositoryFriendship {
    private String url;
    private String username;
    private String password;

    public FriendshipRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Friendship extractFriendship(ResultSet resultSet) throws SQLException {
        Long IDFirstFriend = resultSet.getLong("id_first_friend");
        Long IDSecondFriend = resultSet.getLong("id_second_friend");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        Long requestID = resultSet.getLong("request_id");
        return new Friendship(IDFirstFriend, IDSecondFriend, date, requestID);
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long> friendshipID) {
        if(friendshipID.getRight() == null || friendshipID.getLeft() == null )
            throw new IllegalArgumentException("ID must not be null!");
        String sql ="SELECT * FROM friendships WHERE (id_first_friend = ? and id_second_friend = ?) or (id_first_friend = ? and id_second_friend = ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, friendshipID.getLeft());
            preparedStatement.setLong(2, friendshipID.getRight());
            preparedStatement.setLong(3, friendshipID.getRight());
            preparedStatement.setLong(4, friendshipID.getLeft());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return Optional.of(extractFriendship(resultSet));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        String sql = "SELECT * FROM friendships";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while(resultSet.next())
                friendships.add(extractFriendship(resultSet));
        } catch (SQLException ignored) {}
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        Optional<Friendship> optionalFriendship = findOne(friendship.getId());
        if (optionalFriendship.isPresent())
            return optionalFriendship;
        String sql = "INSERT INTO friendships (id_first_friend, id_second_friend, date, request_id) values (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, friendship.getId().getLeft());
            preparedStatement.setLong(2, friendship.getId().getRight());
            preparedStatement.setDate(3, Date.valueOf(friendship.getDate()));
            preparedStatement.setLong(4, friendship.getRequestID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: insert or update on table \"friendships\" violates foreign key constraint"))
                throw new ValidationException("The users must exist!");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long> friendshipID) {
        Optional<Friendship> optionalFriendship = findOne(friendshipID);
        if(optionalFriendship.isEmpty())
            return optionalFriendship;
        String sql = "DELETE FROM friendships WHERE (id_first_friend = ? and id_second_friend = ?) or (id_first_friend = ? and id_second_friend = ?); ";
        sql += "DELETE FROM friend_request WHERE request_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, friendshipID.getRight());
            preparedStatement.setLong(2, friendshipID.getLeft());
            preparedStatement.setLong(3, friendshipID.getLeft());
            preparedStatement.setLong(4, friendshipID.getRight());
            preparedStatement.setLong(5, optionalFriendship.get().getRequestID());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return optionalFriendship;
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        Optional<Friendship> optionalFriendship = findOne(friendship.getId());
        if(optionalFriendship.isEmpty())
            return Optional.of(friendship);
        String sql = "UPDATE friendships SET date = ? WHERE (id_first_friend = ? AND id_second_friend = ?) or  (id_first_friend = ? and id_second_friend = ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(friendship.getDate()));
            preparedStatement.setLong(2, friendship.getId().getLeft());
            preparedStatement.setLong(3, friendship.getId().getRight());
            preparedStatement.setLong(4, friendship.getId().getRight());
            preparedStatement.setLong(5, friendship.getId().getLeft());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public List<Friendship> getAllFriendshipsForAUser(Long userID) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT * FROM friendships WHERE id_first_friend = ? OR id_second_friend = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            preparedStatement.setLong(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                friendships.add(extractFriendship(resultSet));
        } catch (SQLException ignored) {}
        return friendships;
    }
}
