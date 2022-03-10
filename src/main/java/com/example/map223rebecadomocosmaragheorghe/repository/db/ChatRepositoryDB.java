package com.example.map223rebecadomocosmaragheorghe.repository.db;

import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryChat;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatRepositoryDB implements RepositoryChat {
    private String url;
    private String username;
    private String password;

    public ChatRepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * extracts a chat object from a result set
     *
     * @param resultSet contains id_chat, name, picture url (in this order)
     * @return a chat object corresponding to the result set
     * @throws SQLException if id does not meet requirements
     */
    private Chat extractChat(ResultSet resultSet) throws SQLException {
        Long IDChat = resultSet.getLong("id_chat");
        String name = resultSet.getString("name");
        String picture = resultSet.getString("picture");
        ArrayList<User> participants = getAllParticipants(IDChat);
        Chat chat = new Chat(name, participants, picture);
        chat.setId(IDChat);
        return chat;
    }

    public ArrayList<User> getAllParticipants(Long IdChat) {
        ArrayList<User> participants = new ArrayList<>();
        String sql = "select u.id, u.email, u.first_name, u.last_name, u.password, u.photo_url from chat c inner join destinatar d on c.id_chat = d.id_chat\n" +
                "inner join users u on d.id_user = u.id where c.id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, IdChat);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long IDParticipant = resultSet.getLong("id");
                String emailParticipant = resultSet.getString("email");
                String firstNameParticipant = resultSet.getString("first_name");
                String lastNameParticipant = resultSet.getString("last_name");
                String passwordParticipant = resultSet.getString("password");
                String photoURLParticipant = resultSet.getString("photo_url");
                participants.add(new User(IDParticipant, emailParticipant, firstNameParticipant, lastNameParticipant, passwordParticipant, photoURLParticipant));
            }
            return participants;
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public Optional<Chat> findOne(Long idChat) {
        if (idChat == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select * from chat where id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, idChat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return Optional.of(extractChat(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Chat> findAll() {
        Set<Chat> participants = new HashSet<>();
        String sql = "select * from chat";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Chat chat = extractChat(resultSet);
                participants.add(chat);
            }
        } catch (SQLException ignored) {
        }
        return participants;
    }

    @Override
    public Optional<Chat> save(Chat chat) {
        Optional<Chat> optionalChat;
        if (chat.getId() != null) {
            optionalChat = findOne(chat.getId());
            if (optionalChat.isPresent())
                return optionalChat;
        }
        String sql = "INSERT INTO chat (name, picture) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (chat.getName() == null)
                preparedStatement.setNull(1, Types.VARCHAR);
            else preparedStatement.setString(1, chat.getName());
            preparedStatement.setString(2, chat.getPicture());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                while (generatedKeys.next()) {
                    long IDChat = generatedKeys.getLong(1);
                    String sql2 = "INSERT INTO destinatar(id_chat, id_user) values (?, ?)";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                    chat.getParticipants().forEach(participant -> {
                        try {
                            preparedStatement2.setLong(1, IDChat);
                            preparedStatement2.setLong(2, participant.getId());
                            preparedStatement2.executeUpdate();
                        } catch (SQLException ignored) {
                            ignored.printStackTrace();
                        }
                    });
                }
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Chat> delete(Long idChat) {
        Optional<Chat> optionalChat = findOne(idChat);
        if (optionalChat.isEmpty())
            return optionalChat;
        String sql = "DELETE FROM chat WHERE id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, idChat);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
        return optionalChat;
    }

    @Override
    public Optional<Chat> update(Chat chat) {
        Optional<Chat> optionalChat = findOne(chat.getId());
        if (optionalChat.isEmpty())
            return Optional.of(chat);
        String sql = "UPDATE chat SET name = ?, picture = ? WHERE id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, chat.getName());
            preparedStatement.setString(2, chat.getPicture());
            preparedStatement.setLong(3, chat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> getMessagesFromChat(Long IDChat) {
        Set<Message> messages = new HashSet<>();
        String sql = "select m.id_message, m.expeditor, m.message, m.date, m.id_chat, u.id, u.first_name, u.last_name, u.email, u.password, u.photo_url from messages as m\n" +
                "inner join users as u on m.expeditor = u.id\n" +
                "where id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, IDChat);
            ResultSet resultSet = preparedStatement.executeQuery();
            Long IDMessage, idChat, IDFrom;
            String message, emailFrom, firstNameFrom, lastNameFrom, password, photoUrl;
            LocalDateTime date;
            User from;
            Message theMessage;
            while (resultSet.next()) {
                IDMessage = resultSet.getLong("id_message");
                message = resultSet.getString("message");
                date = resultSet.getTimestamp("date").toLocalDateTime();
                idChat = resultSet.getLong("id_chat");
                IDFrom = resultSet.getLong("id");
                emailFrom = resultSet.getString("email");
                firstNameFrom = resultSet.getString("first_name");
                lastNameFrom = resultSet.getString("last_name");
                password = resultSet.getString("password");
                photoUrl = resultSet.getString("photo_url");
                from = new User(IDFrom, emailFrom, firstNameFrom, lastNameFrom, password, photoUrl);
                theMessage = new Message(from, message, date, idChat);
                theMessage.setId(IDMessage);
                messages.add(theMessage);
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return messages;
    }

    @Override
    public Long getNumberOfParticipants(Long IDChat) {
        String sql = "select count(id_user) as participants from destinatar where id_chat = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, IDChat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getLong("participants");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Message> getLastMessageInChat(Long IDChat) {
        String sql = "select m.id_message, m.message, m.date, m.id_chat, u.id, u.first_name, u.last_name, u.email, u.password, u.photo_url\n" +
                "from messages as m\n" +
                "inner join chat as c on m.id_chat = c.id_chat\n" +
                "inner join users as u on u.id = m.expeditor \n" +
                "where c.id_chat = ?\n" +
                "order by m.date desc\n" +
                "limit 1";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, IDChat);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String photo = resultSet.getString("photo_url");
                User expeditor = new User(email, firstName, lastName, password, photo);
                expeditor.setId(idUser);
                String body = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                Long idMessage = resultSet.getLong("id_message");
                Long idChat = resultSet.getLong("id_chat");
                Message message = new Message(expeditor, body, date, idChat);
                message.setId(idMessage);
                return Optional.of(message);
            }
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Chat> getChatsForAUser(Long IDUser) {
        Set<Chat> chats = new HashSet<>();
        String sql = "select c.id_chat, c.name, c.picture from chat as c inner join destinatar as d on d.id_chat = c.id_chat\n" +
                "where d.id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, IDUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long idChat = resultSet.getLong("id_chat");
                String name = resultSet.getString("name");
                String picture = resultSet.getString("picture");
                Chat chat = new Chat(name, getAllParticipants(idChat), picture);
                chat.setId(idChat);
                chats.add(chat);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return chats;
    }
}