package com.example.map223rebecadomocosmaragheorghe.domain.validators;

import com.example.map223rebecadomocosmaragheorghe.domain.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event event) throws ValidationException {
        String errorMessage = "";
        if(event == null)
            throw  new IllegalArgumentException("Event must be not null!");
        if(event.getName().equals(""))
            errorMessage += "Name is a required field!";
        if(event.getDescription().equals(""))
            errorMessage += "Description is a required field!";
        if(event.getDate() == null)
            errorMessage += "Date is a required field!";
        if(!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
