package com.example.map223rebecadomocosmaragheorghe.repository.db;


import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.repository.Repository;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryUser;

import java.sql.*;
import java.util.*;

public class UserRepositoryDB implements RepositoryUser {
    private String url;
    private String username;
    private String password;

    public UserRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
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

    private Optional<User> findUser(Long ID) {
        if(ID == null)
            throw new IllegalArgumentException("ID must be not null!");
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return Optional.of(extractUser(resultSet));
        } catch (SQLException ignored){}
        return Optional.empty();
    }

    public Optional<User> findUser(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractUser(resultSet));
            }
        } catch (SQLException ignored){}
        return Optional.empty();
    }

    private ArrayList<User> makesFriendList(Long ID){
        ArrayList<User> friends = new ArrayList<>();
        String sql0 = "select u.id, u.first_name, u.last_name, u.email, u.password, u.photo_url from users as u inner join friendships as f on u.id = f.id_second_friend where f.id_first_friend = ? " +
                "union select u.id, u.first_name, u.last_name, u.email, u.password, u.photo_url from users as u inner join friendships as f on u.id = f.id_first_friend where f.id_second_friend = ? ";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setLong(1, ID);
            preparedStatement.setLong(2, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                friends.add(extractUser(resultSet));
        } catch (SQLException ignored) {}
        return friends;
    }

    @Override
    public Optional<User> findOne(Long ID) {
        Optional<User> optionalUser = findUser(ID);
        if(optionalUser.isEmpty())
            return optionalUser;
        optionalUser.get().setFriendsList(makesFriendList(ID));
        return optionalUser;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM users";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(sql)){
            while(resultSet.next()) {
                User user = extractUser(resultSet);
                user.setFriendsList(makesFriendList(user.getId()));
                users.add(user);
            }
        } catch (SQLException ignored) {ignored.printStackTrace();}
        return users;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> optionalUser;
        if(user.getId() != null) {
            optionalUser = findUser(user.getId());
            if (optionalUser.isPresent())
                return optionalUser;
        }
        optionalUser = findUser(user.getEmail());
        if(optionalUser.isPresent())
            return optionalUser;
        String sql = "INSERT INTO users (email, first_name, last_name, password, photo_url) values (?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhotoUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long ID) {
        Optional<User> optionalUser = findUser(ID);
        if(optionalUser.isEmpty())
            return optionalUser;
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, ID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return optionalUser;
    }

    @Override
    public Optional<User> update(User user) {
        Optional<User> optionalUser = findUser(user.getId());
        if(optionalUser.isEmpty())
            return Optional.of(user);
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, password = ?, photo_url =? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhotoUrl());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return findUser(email);
    }

    @Override
    public List<User> findMatching(String name, Long ID) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users where (first_name like '" + name + "%' or last_name like '" + name +"%') and id != ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                User user = extractUser(resultSet);
                users.add(user);
            }
        } catch (SQLException ignored) {ignored.printStackTrace();}
        return users;
    }
}