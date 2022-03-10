package com.example.map223rebecadomocosmaragheorghe.domain.validators;

import com.example.map223rebecadomocosmaragheorghe.domain.User;

public class UserValidator implements Validator<User>{
    /**Validates a User.
     * @param user the User that is validated.
     * @throws ValidationException if user is not a valid User.
     */
    @Override
    public void validate(User user) throws ValidationException, IllegalArgumentException {
        String errorMessage = "";
        if(user == null)
            throw new IllegalArgumentException("User must be not null!");
        if(user.getEmail() == null || "".equals(user.getEmail()))
            errorMessage += "Email is a required field.\n";
        else if(!user.getEmail().matches("[a-zA-Z0-9_.-]+@[a-z]+[.][a-z]+"))
            errorMessage += "Email is invalid!\n";
        if(user.getFirstName() == null || "".equals(user.getFirstName()))
            errorMessage += "First Name is a required field.\n";
        else if(!user.getFirstName().matches("^([A-Z][a-z]*)([ ][A-Z][a-z]*)?$"))
            errorMessage += "First name does not respect the format!\n";
        if(user.getLastName() == null || "".equals(user.getLastName()))
            errorMessage += "Last Name is a required filed.\n";
        else if(!user.getFirstName().matches("^([A-Z][a-z]*)([ ][A-Z][a-z]*)?$"))
            errorMessage += "Last name does not respect the format!\n";
        if(!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
