package com.example.map223rebecadomocosmaragheorghe.domain.validators;

public interface Validator<T> {
    /**Validate an entity.
     * @param entity T the entity that is validated.
     * @throws ValidationException if the entity is not valid.
     */
    void validate(T entity) throws ValidationException;
}