package repository.db;
import domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryDBTest {
    String username = "postgres";
    String password = "parola";
    String url = "jdbc:postgresql://localhost:5432/testTheSocialNetwork";
    Repository<Long, User> userRepository  = new UserRepositoryDB(url, username, password);

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
    void findOne() {
        Optional<User> user = userRepository.findOne(1L);
        assertTrue(user.isPresent());
        assertEquals(user.get().getId(), 1L);
        assertEquals(user.get().getEmail(), "stefan@yahoo.com");
        assertEquals(user.get().getFirstName(), "Stefan");
        assertEquals(user.get().getLastName(), "Bodea");
        user = userRepository.findOne(3L);
        assertTrue(user.isEmpty());
        assertThrows(IllegalArgumentException.class, () -> userRepository.findOne(null));
    }

    @Test
    void findAll() {
        Iterable<User> users = userRepository.findAll();
        ArrayList<User> userList = new ArrayList<>();
        users.forEach(userList::add);
        assertEquals(userList.size(), 2);
        assertEquals(userList.get(0).getId(), 1L);
        assertEquals(userList.get(0).getEmail(), "stefan@yahoo.com");
        assertEquals(userList.get(1).getId(), 2L);
        assertEquals(userList.get(1).getEmail(), "emily@yahoo.com");
    }

    @Test
    void save() {
        Optional<User> optionalUser = userRepository.save(new User("mara@yahoo.com", "Mara", "Gheorghe"));
        assertTrue(optionalUser.isEmpty());
        optionalUser = userRepository.save(new User("mara@yahoo.com", "M", "M"));
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getId(), 3L);
        assertEquals(optionalUser.get().getEmail(), "mara@yahoo.com");
        optionalUser = userRepository.save(new User(2L, "a@a.com", "A", "A"));
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getEmail(), "emily@yahoo.com");
    }

    @Test
    void delete() {
        Optional<User> optionalUser = userRepository.delete(1L);
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getId(), 1L);
        assertEquals(optionalUser.get().getEmail(), "stefan@yahoo.com");
        optionalUser = userRepository.delete(4L);
        assertTrue(optionalUser.isEmpty());
        assertThrows(IllegalArgumentException.class, () -> userRepository.delete(null));
    }

    @Test
    void update() {
        Optional<User> optionalUser = userRepository.update(new User(1L, "stefan_luca@yahoo.com", "Stefan Luca", "Bodea"));
        assertTrue(optionalUser.isEmpty());
        optionalUser = userRepository.findOne(1L);
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getEmail(), "stefan_luca@yahoo.com");
        assertEquals(optionalUser.get().getFirstName(), "Stefan Luca");
        assertEquals(optionalUser.get().getLastName(), "Bodea");
        optionalUser = userRepository.update(new User(5L, "a@a.com", "A", "A"));
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getId(), 5L);
        assertEquals(optionalUser.get().getEmail(), "a@a.com");
    }
}