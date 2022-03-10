package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Constants;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

class FriendshipTest {
    Friendship friendship;
    Friendship friendshipDate;
    LocalDate date;
    @BeforeEach
    void setUp() {
        friendship = new Friendship(1L, 2L, 1L);
        friendshipDate = new Friendship(2L, 3L, LocalDate.now(), 2L);
        date = LocalDate.now();
    }

    @Test
    void getDate() {
        assertEquals(friendship.getDate(), date);
        assertEquals(friendshipDate.getDate(), date);
    }

    @Test
    void setDate() {
        LocalDate now = LocalDate.of(2020, 9, 10);
        friendship.setDate(LocalDate.of(2020, 9, 10));
        assertEquals(friendship.getDate(), now);
        friendshipDate.setDate(LocalDate.of(2020, 9, 10));
        assertEquals(friendshipDate.getDate(), now);
    }

    @Test
    void testToString() {
        assertEquals(friendship.toString(), friendship.getId().getLeft() + " and " + friendship.getId().getRight() + "\nFROM: "
                + friendship.getDate().format(Constants.DATE_TIME_FORMATTER) + "\n");
        assertEquals(friendshipDate.toString(), friendshipDate.getId().getLeft() + " and " + friendshipDate.getId().getRight() + "\nFROM: "
                + friendshipDate.getDate().format(Constants.DATE_TIME_FORMATTER) + "\n");
    }
}