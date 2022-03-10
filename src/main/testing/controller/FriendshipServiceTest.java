package controller;

import domain.Friendship;
import domain.Tuple;
import domain.validators.FriendshipValidator;
import domain.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.db.FriendshipRepositoryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipServiceTest {
    String username = "postgres";
    String password = "parola";
    String url = "jdbc:postgresql://localhost:5432/testTheSocialNetwork";
    Repository<Tuple<Long>, Friendship> friendshipRepository  = new FriendshipRepositoryDB(url, username, password);
    FriendshipService friendshipService = new FriendshipService(friendshipRepository, new FriendshipValidator());

    @BeforeEach
    void setUp() {
        String sql0 = "create table users (id bigint primary key generated always as identity, email varchar(50) unique, first_name varchar(50), last_name varchar(50)); " +
                "insert into users (email, first_name, last_name) values ('stefan@yahoo.com', 'Stefan', 'Bodea'), ('emily@yahoo.com', 'Emilia', 'Boamba'), ('mara@yahoo.com', 'Mara', 'Gheorghe'), ('a@a.com', 'A', 'A');";
        String sql1 = "create table friendships (id_first_friend bigint, id_second_friend bigint, date date, request_id bigint, constraint fk_key_user1 foreign key(id_first_friend)" +
                " references users(id) on delete cascade on update cascade, " +
                "constraint fk_key_user2 foreign key(id_second_friend) references users(id) on delete cascade on update cascade," +
                "constraint pk_friendships primary key (id_first_friend, id_second_friend));" +
                "insert into Friendships (id_first_friend, id_second_friend, date, request_id) values (1, 3, NOW(), 1), (3, 2, NOW(), 2);";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.createStatement().executeQuery(sql0 + sql1);
        } catch (SQLException ignored){}
    }

    @AfterEach
    void tearDown() {
        String sql = "drop table friendships; drop table users";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.createStatement().executeQuery(sql);
        } catch (SQLException ignored){}
    }


    @Test
    void addFriendship() {
        assertTrue(friendshipService.addFriendship(1L, 4L).isEmpty());
        assertTrue(friendshipService.addFriendship(1L, 4L).isPresent());
        assertThrows(ValidationException.class, () -> friendshipService.addFriendship(1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.addFriendship(null, null));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.addFriendship(1L, null));
    }

    @Test
    void deleteFriendship() {
        assertTrue(friendshipService.deleteFriendship(2L, 3L).isPresent());
        assertTrue(friendshipService.deleteFriendship(2L, 3L).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> friendshipService.deleteFriendship(null, null));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.deleteFriendship(null, 1L));
    }

    @Test
    void updateFriendship() {
        assertTrue(friendshipService.updateFriendship(3L, 2L, LocalDate.now()).isEmpty());
        assertTrue(friendshipService.updateFriendship(1L, 4L, LocalDate.now()).isPresent());
        assertThrows(ValidationException.class, () -> friendshipService.updateFriendship(1L, 3L, null));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.updateFriendship(null, null, null));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.updateFriendship(1L, null, null));
    }

    @Test
    void findOneFriendship() {
        assertTrue(friendshipService.findOneFriendship(2L, 3L).isPresent());
        assertTrue(friendshipService.findOneFriendship(2L, 1L).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> friendshipService.findOneFriendship(null, null));
        assertThrows(IllegalArgumentException.class, () -> friendshipService.findOneFriendship(null, 1L));
    }

    @Test
    void findAllFriendships() {
        Iterable<Friendship> friendships = friendshipService.findAllFriendships();
        ArrayList<Friendship> friendshipList = new ArrayList<>();
        friendships.forEach(friendshipList::add);
        assertEquals(friendshipList.size(), 2);
    }
}