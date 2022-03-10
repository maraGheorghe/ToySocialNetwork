package com.example.map223rebecadomocosmaragheorghe.domain.validators;

public class ValidationException extends RuntimeException {
    /**Class constructor specifying exception's message.
     * @param message a String representing the exception's message.
     */
    public ValidationException(String message) {
        super(message);
    }
}
