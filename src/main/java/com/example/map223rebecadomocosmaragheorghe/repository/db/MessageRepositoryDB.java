package com.example.map223rebecadomocosmaragheorghe.repository.db;


import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryMessage;
import java.util.List;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageRepositoryDB implements RepositoryMessage {
    private String url;
    private String username;
    private String password;

    public MessageRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * @param resultSet - contains id, id of sender, string of message and a timestamp (in this order)
     * @return the Message object corresponding to the result set
     * @throws SQLException if id does not meet requirements
     */
    private Message extractMessage(ResultSet resultSet) throws SQLException {
        Long IDMessage = resultSet.getLong("id_message");
        String message = resultSet.getString("message");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        Long idChat = resultSet.getLong("id_chat");
        Long IDFrom = resultSet.getLong("user_id");
        String emailFrom = resultSet.getString("email");
        String firstNameFrom = resultSet.getString("first_name");
        String lastNameFrom = resultSet.getString("last_name");
        String password = resultSet.getString("password");
        String photoUrl = resultSet.getString("photo_url");
        User from = new User(IDFrom, emailFrom, firstNameFrom, lastNameFrom, password, photoUrl);
        Message theMessage = new Message(from, message, date, idChat);
        theMessage.setId(IDMessage);
        return theMessage;
    }

    private Long findID(Message message) {
        String sql = "select id_message from messages where expeditor = ? and date = ? and message = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, message.getFrom().getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(message.getDate()));
            preparedStatement.setString(3, message.getBody());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                return resultSet.getLong("id");
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Message> findOne(Long ID) {
        if (ID == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select m.id_message, m.message, m.date, m.id_chat, u.id as user_id, u.email, u.first_name, u.last_name, u.password, u.photo_url from messages as m " +
                "inner join users as u on m.expeditor = u.id where m.id_message = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return Optional.of(extractMessage(resultSet));
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        String sql = "select m.id_message, m.message, m.date, m.id_chat, u.id as user_id, u.email, u.first_name, u.last_name, u.password, u.photo_url from messages m " +
                "inner join users u on m.expeditor = u.id;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Message message = extractMessage(resultSet);
                messages.add(message);
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message message) {
        Optional<Message> optionalMessage;
        if (message.getId() != null) {
            optionalMessage = findOne(message.getId());
            if (optionalMessage.isPresent())
                return optionalMessage;
        }
        String sql = "INSERT INTO messages (expeditor, message, date, id_chat) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, message.getFrom().getId());
            preparedStatement.setString(2, message.getBody());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(message.getDate()));
            preparedStatement.setLong(4, message.getIdChat());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long ID) {
        Optional<Message> optionalMessage = findOne(ID);
        if (optionalMessage.isEmpty())
            return optionalMessage;
        String sql = "DELETE FROM messages WHERE id_message = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, ID);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return optionalMessage;
    }

    @Override
    public Optional<Message> update(Message message) {
        Optional<Message> optionalMessage = findOne(message.getId());
        if (optionalMessage.isEmpty())
            return Optional.of(message);
        String sql = "UPDATE messages SET expeditor = ?, message = ?, date = ? WHERE id_message = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, message.getFrom().getId());
            preparedStatement.setString(2, message.getBody());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(message.getDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {}
        return Optional.empty();
    }

    @Override
    public Iterable<Message> getMessagesForOne(Long userID) {
        Set<Message> messages = new HashSet<>();
        String sql = "select m.id_message, m.message, m.date, m.id_chat, u.id as user_id, u.email, u.first_name, u.last_name from messages m " +
                "inner join users u on m.expeditor = u.id inner join destinatar d on m.id_chat = d.id_chat where d.id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = extractMessage(resultSet);
                messages.add(message);
            }
        } catch (SQLException ignored) {}
        return messages;
    }

    @Override
    public Iterable<Message> getMessagesBetweenTwoUsers(Long firstID, Long secondID) {
        Set<Message> messages = new HashSet<>();
        String sql = "SELECT DISTINCT m.id_message, m.message, m.date, m.id_chat, u.id as user_id, u.email, u.first_name, u.last_name FROM messages m " +
                "INNER JOIN users u on m.expeditor = u.id INNER JOIN destinatar d ON m.id_chat = d.id_chat WHERE m.expeditor = ? AND d.id_user = ? OR m.expeditor = ? AND d.id_user = ?";
        try( Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, firstID);
            preparedStatement.setLong(2, secondID);
            preparedStatement.setLong(3, secondID);
            preparedStatement.setLong(4, firstID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Message message = extractMessage(resultSet);
                messages.add(message);
            }
        } catch (SQLException ignored) {}
        return messages;
    }

    @Override
    public Iterable<Message> getMessagesWithOne(Long userID) {
        Set<Message> messages = new HashSet<>();
        String sql = "select m.id_message, m.message, m.date, m.id_chat, u.id as user_id, u.email, u.first_name, u.last_name from messages m " +
                "inner join users u on m.expeditor = u.id inner join destinatar d on m.id_chat = d.id_chat where d.id_user = ? or m.expeditor = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userID);
            preparedStatement.setLong(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = extractMessage(resultSet);
                messages.add(message);
            }
        } catch (SQLException ignored) {}
        return messages;
    }


}