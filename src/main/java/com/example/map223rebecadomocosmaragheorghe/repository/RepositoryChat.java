package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;

import java.util.ArrayList;
import java.util.Optional;

public interface RepositoryChat extends Repository<Long, Chat> {

    /**
     * @param IDChat the ID of a chat
     * @return the messages from the chat
     */
    Iterable<Message> getMessagesFromChat(Long IDChat);

    /**
     * @param IDChat the ID of a chat
     * @return the number of users in chat
     */
    Long getNumberOfParticipants(Long IDChat);

    /**
     * @param IDChat the ID of a chat
     * @return the last message sent in the chat
     */
    Optional<Message> getLastMessageInChat(Long IDChat);

    /**
     * @param IDUser the ID of a user
     * @return all chats of a user
     */
    Iterable<Chat> getChatsForAUser(Long IDUser);

    /**
     * @param IdChat the id of a chat
     * @return all the participants in the chat
     */
    ArrayList<User> getAllParticipants(Long IdChat);
}
