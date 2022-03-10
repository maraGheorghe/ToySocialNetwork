package com.example.map223rebecadomocosmaragheorghe.domain;

import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity<Long> {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String photoUrl;
    public List<User> friendsList;


    /**
     * Class constructor for a user.
     *
     * @param email     a String representing the user's email.
     * @param firstName a String representing the user's first name.
     * @param lastName  a String representing the user's last name.
     */
    public User(String email, String firstName, String lastName, String password, String photoUrl) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photoUrl = photoUrl;
        friendsList = new ArrayList<>();
    }

    /**
     * Class constructor for a user.
     *
     * @param ID        a Long representing the user's ID.
     * @param email     a String representing the user's email.
     * @param firstName a String representing the user's first name.
     * @param lastName  a String representing the user's last name.
     */
    public User(Long ID, String email, String firstName, String lastName, String password, String photoUrl) {
        setId(ID);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photoUrl = photoUrl;
        friendsList = new ArrayList<>();
    }

    /**
     * Gets the user's email.
     *
     * @return a String representing the user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param email a String representing the user's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's first name.
     *
     * @return a String representing the user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName a String representing the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return a String representing the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName a String representing the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Gets the user's list of friends.
     *
     * @return a List of users representing the user's friends.
     */
    public List<User> getFriendsList() {
        return friendsList;
    }

    /**
     * Sets the user's list of friends.
     *
     * @param friendsList a List of users representing the user's friends.
     */
    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    /**
     *
     * @return the complete name of the user (<firstname> <lastname>)
     */
    public String getCompleteName(){return firstName + " " + lastName;}

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public UserDTO toDTO() {
        return new UserDTO(firstName, lastName, email, photoUrl);
    }
}