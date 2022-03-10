package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    @BeforeEach
    void setUp() {
        user = new User(1L, "a", "A", "B");
    }

    @Test
    void getEmail() {
        assertEquals(user.getEmail(), "a");
    }

    @Test
    void setEmail() {
        user.setEmail("b");
        assertEquals(user.getEmail(), "b");
    }

    @Test
    void getFirstName() {
        assertEquals(user.getFirstName(), "A");
    }

    @Test
    void setFirstName() {
        user.setFirstName("AA");
        assertEquals(user.getFirstName(), "AA");
    }

    @Test
    void getLastName() {
        assertEquals(user.getLastName(), "B");
    }

    @Test
    void setLastName() {
        user.setLastName("BB");
        assertEquals(user.getLastName(), "BB");
    }

    @Test
    void getFriendsList() {
        var userFriends = new ArrayList<User>();
        assertEquals(user.getFriendsList(), userFriends);
    }

    @Test
    void setFriendsList() {
        User friend = new User(2L, "b", "B", "C");
        ArrayList<User> friends = new ArrayList<>();
        friends.add(friend);
        user.setFriendsList(friends);
        assertEquals(user.getFriendsList(), friends);
    }

    @Test
    void testToString() {
        assertEquals(user.toString(), "ID: 1\nEmail: a\nFirst Name: A\nLastName: B\nFriends: ");
    }
}