package com.example.map223rebecadomocosmaragheorghe.domain.validators;

import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;

public class MessageValidator implements Validator<Message> {
    private final Validator<User> userValidator = new UserValidator();

    @Override
    public void validate(Message message) throws ValidationException {
        if (message == null)
            throw new ValidationException("Message must be not null!");
        if (message.getFrom() == null)
            throw new ValidationException("Sender must be not null!");
        if (message.getBody() == null)
            throw new ValidationException("Message body must be not null!");
        if (message.getDate() == null)
            throw new ValidationException("Date and time of message must be not null");
        userValidator.validate(message.getFrom());
    }
}
