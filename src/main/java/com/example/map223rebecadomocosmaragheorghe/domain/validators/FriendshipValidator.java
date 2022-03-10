package com.example.map223rebecadomocosmaragheorghe.domain.validators;

import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    /**Validates a friendship.
     * @param friendship the Friendship that is validated.
     * @throws ValidationException if friendship is not a valid Friendship.
     */
    @Override
    public void validate(Friendship friendship) throws ValidationException, IllegalArgumentException {
        String errorMessage = "";
        if(friendship == null)
            throw  new IllegalArgumentException("Friendship must be not null!");
        if(friendship.getId() == null)
            throw new IllegalArgumentException("ID must be not null!");
        if(friendship.getId().getLeft() == null)
            throw new IllegalArgumentException("First friend's ID must be not null!");
        if(friendship.getId().getRight() == null)
            throw new IllegalArgumentException("Second friend's ID must be not null!");
        if(friendship.getId().getRight().equals(friendship.getId().getLeft()))
            errorMessage += "You can't be your own friend...:(\n";
        if(friendship.getDate() == null)
            errorMessage += "Date is a required field!";
        if(!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}