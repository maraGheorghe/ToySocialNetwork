package domain.validators;

import domain.Friendship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipValidatorTest {
    Friendship friendshipValid;
    Friendship friendshipInvalid;
    Friendship friendshipInvalid1;
    FriendshipValidator friendshipValidator;
    @BeforeEach
    void setUp() {
        friendshipValid = new Friendship(1L, 2L, 1L);
        friendshipInvalid = new Friendship(1L, 1L, 2L);
        friendshipInvalid1 = new Friendship(1L, 2L, 3L);
        friendshipValidator = new FriendshipValidator();
    }

    @Test
    void validate() {
        assertThrows(ValidationException.class, () ->{
            friendshipValidator.validate(friendshipInvalid);
        });
        assertDoesNotThrow(() -> {
            friendshipValidator.validate(friendshipValid);
        });
    }
}