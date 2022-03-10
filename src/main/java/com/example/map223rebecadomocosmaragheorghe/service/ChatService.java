package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryChat;
import com.example.map223rebecadomocosmaragheorghe.repository.db.ChatRepositoryDB;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.MessageDTO;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import com.example.map223rebecadomocosmaragheorghe.utils.events.ChangeEventType;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendRequestChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.events.MessageChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observable;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatService implements Observable<MessageChangeEvent> {

    RepositoryChat repository;
    PropertyChangeSupport propertyChangeSupport;
    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public ChatService(ChatRepositoryDB chatRepositoryDB) {
        this.repository = chatRepositoryDB;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * @param chatDTO a dto object containing name of chat, participants in chat and chat picture
     */
    public void saveChat(ChatDTO chatDTO) throws Exception {
        Chat chat = new Chat(chatDTO);
        if (chatDTO.getPicture() == null) {
            chat.setPicture(Constants.groupPhoto);
        }
        Optional<Chat> optionalChat = repository.save(chat);
        propertyChangeSupport.firePropertyChange("chat", null, null);
        if (optionalChat.isEmpty()) notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
        else notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, optionalChat.get()));
        if (optionalChat.isPresent()) throw new Exception("Chat already exists.");
    }

    /**
     * @param IDChat id of a chat
     */
    public void deleteChat(Long IDChat) {
        Optional<Chat> optionalChat = repository.delete(IDChat);
        if (optionalChat.isPresent()) notifyObservers(new MessageChangeEvent(ChangeEventType.DELETE, optionalChat.get()));
        else notifyObservers(new MessageChangeEvent(ChangeEventType.DELETE, null));
    }

    /**
     * @param IDChat id of a chat
     * @return {@code Optional} a chat
     */
    public Optional<Chat> findOne(Long IDChat){return repository.findOne(IDChat);}

    /**
     * @return all chats
     */
    public Iterable<Chat> findAll(){return repository.findAll();}

    /**
     * @param IDChat id of a chat
     * @return the messages from the chat
     */
    public List<MessageDTO> findMessagesFromChat(Long IDChat) {
        return StreamSupport.stream(repository.getMessagesFromChat(IDChat).spliterator(), false)
                .map(message ->  new MessageDTO(message.getBody(), message.getFrom(), message.getDate(), message.getIdChat()))
                .collect(Collectors.toList());
    }

    /**
     * @param IDUser the ID of a user
     * @return all chats of a user
     */
    public Iterable<ChatDTO> findChatsOfUser(Long IDUser) {
        return StreamSupport.stream(repository.getChatsForAUser(IDUser).spliterator(), false)
                .map(chat -> {
                    ChatDTO chatDTO = new ChatDTO(chat.getName(), chat.getParticipants(), chat.getPicture());
                    chatDTO.setId(chat.getId());
                    Optional<Message> lastMessage = repository.getLastMessageInChat(chat.getId());
                    MessageDTO lastMessageDTO;
                    if (lastMessage.isPresent()) {
                        lastMessageDTO = new MessageDTO(lastMessage.get().getBody(), lastMessage.get().getFrom(),lastMessage.get().getDate(),lastMessage.get().getIdChat());
                        chatDTO.setLastMessage(lastMessageDTO);
                    }
                    else chatDTO.setLastMessage(new MessageDTO(null, null, null, null));
                    if (repository.getNumberOfParticipants(chat.getId()).equals(2l)) {
                        List<User> user = repository.getAllParticipants(chat.getId()).stream().filter(user1 -> !user1.getId().equals(IDUser)).collect(Collectors.toList());
                        chatDTO.setName(user.get(0).getCompleteName());
                        chatDTO.setPicture(user.get(0).getPhotoUrl());
                    }
                    return chatDTO;
                })
                .collect(Collectors.toList());
    }

    public Iterable<Chat> getChatsForOne(Long userID) {
        return repository.getChatsForAUser(userID);
    }

    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        observers.forEach(o -> o.update(t));
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }
}
