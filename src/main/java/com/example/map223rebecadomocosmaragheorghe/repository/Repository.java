package com.example.map223rebecadomocosmaragheorghe.repository;
import com.example.map223rebecadomocosmaragheorghe.domain.Entity;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;

import java.util.Optional;

/**
 * CRUD' operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    /**Finds an Entity by ID.
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    Optional<E> findOne(ID id);

    /**Finds all the Entities.
     * @return all entities
     */
    Iterable<E> findAll();

    /**Saves an Entity.
     * @param entity
     *         entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     *                             - the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    Optional<E> save(E entity);


    /**Deletes an Entity.
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return an {@code Optional}
     *            - null if there is no entity with the given id,
     *            - the removed entity, otherwise
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    Optional<E> delete(ID id);

    /**Updates an Entity.
     * @param entity
     *          entity must not be null
     * @return  an {@code Optional}
     *             - null if the entity was updated
     *             - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    Optional<E> update(E entity);

}