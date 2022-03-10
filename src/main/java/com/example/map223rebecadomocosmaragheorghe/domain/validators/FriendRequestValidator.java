package com.example.map223rebecadomocosmaragheorghe.domain.validators;

import com.example.map223rebecadomocosmaragheorghe.domain.FriendRequest;
import com.example.map223rebecadomocosmaragheorghe.domain.Status;

public class FriendRequestValidator implements Validator<FriendRequest> {

    @Override
    public void validate(FriendRequest friendRequest) throws ValidationException {
        String errorMessage = "";
        if(friendRequest == null)
            throw  new IllegalArgumentException("Friend request must be not null!");
        if(friendRequest.getFrom() == null)
            throw new IllegalArgumentException("Requester ID must be not null!");
        if(friendRequest.getTo() == null)
            throw new IllegalArgumentException("Receiver ID must be not null!");
        if(friendRequest.getFrom().equals(friendRequest.getTo()))
            errorMessage += "You can't be your own friend...:(\n";
        if(!(friendRequest.getStatus().equals(Status.approved)
                || friendRequest.getStatus().equals(Status.pending)
                || friendRequest.getStatus().equals(Status.rejected)))
            errorMessage += "Invalid status!\n";
        if(!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
