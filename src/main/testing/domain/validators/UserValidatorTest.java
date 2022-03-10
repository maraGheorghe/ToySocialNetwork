package domain.validators;

import domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    User userValid;
    User userInvalid;
    UserValidator userValidator;
    @BeforeEach
    void setUp() {
        userValid = new User(1L, "a@a.a", "A B", "Bc");
        userInvalid = new User(0L, "a", "a", "a");
        userValidator =  new UserValidator();
    }

    @Test
    void validate() {
        assertThrows(ValidationException.class, () -> {
            userValidator.validate(userInvalid);
        });
        assertDoesNotThrow(()->{
            userValidator.validate(userValid);
        });
    }
}