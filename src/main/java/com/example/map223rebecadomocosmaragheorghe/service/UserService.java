package com.example.map223rebecadomocosmaragheorghe.service;

import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.Validator;
import com.example.map223rebecadomocosmaragheorghe.repository.RepositoryUser;
import java.util.List;
import java.util.Optional;

public class UserService {

    private RepositoryUser userRepository;
    Validator<User> validator;

    /**Class constructor.
     * @param userRepository Repository representing the repository for users.
     */
    public UserService(RepositoryUser userRepository, Validator<User> validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    /**Adds a user.
     * @param email a String representing the user's email.
     * @param firstName a String representing the user's first name.
     * @param lastName a String representing the user's last name.
     * @return an {@code Optional} encapsulating the user if that already exist, null otherwise.
     * @throws ValidationException if the user is not valid.
     */
    public Optional<User> addUser(String email, String firstName, String lastName, String password, String photoUrl) {
        User user = new User(email, firstName, lastName, password, photoUrl);
        validator.validate(user);
        return userRepository.save(user);
    }

    /**Removes a user.
     * @param ID a Long representing the user's ID.
     * @return an {@code Optional} encapsulating the user that has been deleted or null if the user does not exist.
     */
    public Optional<User> deleteUser(Long ID){
        return userRepository.delete(ID);
    }

    /**Updates a user.
     * @param ID a Long representing the user's ID.
     * @param email a String representing the user's email.
     * @param firstName a String representing the user's first name.
     * @param lastName a String representing the user's last name.
     * @return an {@code Optional} encapsulating the user if that doesn't exist, null otherwise.
     */
    public Optional<User> updateUser(Long ID, String email, String firstName, String lastName, String password, String photoUrl){
        User user = new User(ID, email, firstName, lastName, password, photoUrl);
        validator.validate(user);
        return userRepository.update(user);
    }

    /**Finds a user.
     * @param ID a Long representing the user's ID.
     * @return an {@code Optional} encapsulating the user that has the given attributes or null if it does not exist.
     */
    public Optional<User> findOneUser(Long ID){
        return userRepository.findOne(ID);
    }

    /**Finds all the users that are registered.
     * @return an Iterable of users representing all the users.
     */
    public Iterable<User> findAllUsers(){
        return userRepository.findAll();
    }

    public List<User> findMatching(String name, Long ID) {
        return userRepository.findMatching(name, ID);
    }

    public Optional<User> findOneByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
