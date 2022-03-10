package com.example.map223rebecadomocosmaragheorghe.domain;
import java.util.List;

public class Community{
    List<User> users;

    /**Class constructor for a community with specified list of users.
     * @param users the list of users
     */
    public Community(List<User> users) {
        this.users = users;
    }

    /**Gets the community's list of users.
     * @return a List of users representing the list of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**Sets the community's list of users.
     * @param users a List of objects users containing the community's list of users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}