package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.Message;

public interface RepositoryMessage extends Repository<Long, Message> {

    /**
     * @param ID of a user
     * @return the messages that a user has
     */
    Iterable<Message> getMessagesForOne(Long ID);


    /**
     * @param firstID the ID of a user
     * @param secondID the ID of a user
     * @return the messages between two users
     */
    Iterable<Message> getMessagesBetweenTwoUsers(Long firstID, Long secondID);

    /**
     * @param ID the ID of a user
     * @return the messages where a user appears
     */
    Iterable<Message> getMessagesWithOne(Long ID);


}
