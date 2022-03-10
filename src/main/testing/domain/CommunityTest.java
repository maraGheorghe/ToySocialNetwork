package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommunityTest {
    Community community;
    @BeforeEach
    void setUp() {
        community = new Community(Arrays.asList(new User(1L, "a", "A", "A"), new User(2L, "b", "B", "B")));
    }

    @Test
    void getUsers() {
        assertEquals(community.getUsers().get(0).getId(), 1L);
        assertEquals(community.getUsers().get(1).getId(), 2L);
    }

    @Test
    void setUsers() {
        community.setUsers(Arrays.asList(new User(3L, "c", "C", "C")));
        assertEquals(community.getUsers().size(), 1);
        assertEquals(community.getUsers().get(0).getId(), 3L);
    }
}