package controller;

import domain.User;
import domain.validators.UserValidator;
import domain.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.db.UserRepositoryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    String username = "postgres";
    String password = "parola";
    String url = "jdbc:postgresql://localhost:5432/testTheSocialNetwork";
    Repository<Long, User> userRepository  = new UserRepositoryDB(url, username, password);
    UserService userService = new UserService(userRepository, new UserValidator());

    @BeforeEach
    void setUp() {
        String sql = "create table users (id bigint primary key generated always as identity, email varchar(50) unique, first_name varchar(50), last_name varchar(50)); " +
                "insert into users (email, first_name, last_name) values ('stefan@yahoo.com', 'Stefan', 'Bodea'), ('emily@yahoo.com', 'Emilia', 'Boamba');";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored){}
    }

    @AfterEach
    void tearDown() {
        String sql = "drop table users";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored){}
    }

    @Test
    void addUser() {
        assertTrue(userService.addUser("a@a.com", "A", "A").isEmpty());
        assertTrue(userService.addUser("a@a.com", "A", "A").isPresent());
        assertThrows(ValidationException.class, () -> userService.addUser(null, null, null));
        assertThrows(ValidationException.class, () -> userService.addUser("a", "a", "a"));
    }

    @Test
    void deleteUser() {
        assertTrue(userService.deleteUser(1L).isPresent());
        assertTrue(userService.deleteUser(3L).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }

    @Test
    void updateUser() {
        assertTrue(userService.updateUser(2L, "a@a.com", "A", "A").isEmpty());
        assertTrue(userService.updateUser(4L,"a@a.com", "A", "A").isPresent());
        assertThrows(ValidationException.class, () -> userService.updateUser(null,null, null, null));
        assertThrows(ValidationException.class, () -> userService.updateUser(1L,"a", "a", "a"));
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null,"a@a.com", "A", "A"));
    }

    @Test
    void findOneUser() {
        assertTrue(userService.findOneUser(1L).isPresent());
        assertTrue(userService.findOneUser(3L).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> userService.findOneUser(null));
    }

    @Test
    void findAllUsers() {
        Iterable<User> users = userService.findAllUsers();
        ArrayList<User> usersList = new ArrayList<>();
        users.forEach(usersList::add);
        assertEquals(usersList.size(), 2);
    }
}