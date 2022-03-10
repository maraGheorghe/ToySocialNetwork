package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.Validator;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryMessage;
import com.example.map223rebecadomocosmaragheorghe.utils.events.ChangeEventType;
import com.example.map223rebecadomocosmaragheorghe.utils.events.MessageChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observable;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;

import java.util.*;

public class MessageService implements Observable<MessageChangeEvent> {

    private RepositoryMessage messageRepository;
    private Validator<Message> messageValidator;
    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public MessageService(RepositoryMessage messageRepository, Validator<Message> messageValidator) {
        this.messageRepository = messageRepository;
        this.messageValidator = messageValidator;
    }

    public void saveMessage(Message message) {
        messageValidator.validate(message);
        messageRepository.save(message);
        notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
    }

    public Optional<Message> findMessage(Long ID) {
        return messageRepository.findOne(ID);
    }

    public Optional<Message> findMessageForAUser(Long messageID, Long userID){
        List<Message> messagesForAUser = new ArrayList<>();
        messageRepository.getMessagesForOne(userID).forEach(messagesForAUser::add);
        return messagesForAUser
                .stream()
                .filter(message -> message.getId().equals(messageID))
                .findFirst();
    }

    public Iterable<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public List<Long> getUsersWithConversation(Long userID) {
        List<Message> messagesForAUser = new ArrayList<>();
        List<Long> users = new ArrayList<>();
        messageRepository.getMessagesWithOne(userID).forEach(messagesForAUser::add);
//        messagesForAUser
//                .forEach(message -> {
//                    if(!message.getFrom().getId().equals(userID))
//                        users.add(message.getFrom().getId());
//                    else message.getTo().forEach(user -> users.add(user.getId()));
//                });
        return new HashSet<>(users).stream().toList();
    }

    public Iterable<Message> getMessagesOfAUser(Long userID) {return messageRepository.getMessagesForOne(userID);}

    public Iterable<Message> getConversationBetweenUsers(Long firstUser, Long secondUser) {
        List<Message> messagesBetweenTwo = new ArrayList<>();
        messageRepository.getMessagesBetweenTwoUsers(firstUser, secondUser).forEach(messagesBetweenTwo::add);
//        return messagesBetweenTwo
//                .stream()
//                .filter(message -> message.getTo().size() == 1)
//                .sorted(Comparator.comparing(Message::getDate))
//                .toList();
        return null;
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

//    public void replyAll(MessageDto replyMessage) {
//        Message message = new Message(replyMessage.getFrom(), replyMessage.getTo(), replyMessage.getBody(), replyMessage.getDate());
//        messageValidator.validate(message);
//        message.setOriginalMessage(replyMessage.getOriginalMessage());
//        List<User> to = message.getTo();
//        replyMessage.getOriginalMessage().getTo().stream().filter(user -> !user.getId().equals(replyMessage.getFrom().getId())).forEach(to::add);
//        to.add(message.getFrom());
//        messageRepository.save(message);
//    }

}
