package repository.db;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipRepositoryDBTest {
    String username = "postgres";
    String password = "parola";
    String url = "jdbc:postgresql://localhost:5432/testTheSocialNetwork";
    Repository<Tuple<Long>, Friendship> friendshipRepository  = new FriendshipRepositoryDB(url, username, password);

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
    void findUser(){
        Repository<Long, User> userRepository  = new UserRepositoryDB(url, username, password);
        Optional<User> optionalUser = userRepository.findOne(3L);
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getFriendsList().size(), 2);
        assertEquals(optionalUser.get().getFriendsList().get(1).getId(), 1L);
        optionalUser = userRepository.findOne(4L);
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getFriendsList().size(), 0);
    }

    @Test
    void findOne() {
        Optional<Friendship> optionalFriendship = friendshipRepository.findOne(new Tuple<>(1L, 3L));
        assertTrue(optionalFriendship.isPresent());
        assertEquals(optionalFriendship.get().getId().getLeft(), 1L);
        assertEquals(optionalFriendship.get().getId().getRight(), 3L);
        assertEquals(optionalFriendship.get().getDate(), LocalDate.now());
        optionalFriendship = friendshipRepository.findOne(new Tuple<>(3L, 1L));
        assertTrue(optionalFriendship.isPresent());
        optionalFriendship = friendshipRepository.findOne(new Tuple<>(2L, 1L));
        assertTrue(optionalFriendship.isEmpty());
        optionalFriendship = friendshipRepository.findOne(new Tuple<>(10L, 11L));
        assertTrue(optionalFriendship.isEmpty());
    }

    @Test
    void findAll() {
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        ArrayList<Friendship> friendshipList = new ArrayList<>();
        friendships.forEach(friendshipList::add);
        assertEquals(friendshipList.size(), 2);
        assertEquals(friendshipList.get(0).getId().getLeft(), 3L);
        assertEquals(friendshipList.get(0).getId().getRight(), 2L);
        assertEquals(friendshipList.get(1).getId().getLeft(), 1L);
        assertEquals(friendshipList.get(1).getId().getRight(), 3L);
    }

    @Test
    void save() {
        Optional<Friendship> optionalFriendship = friendshipRepository.save(new Friendship(2L, 4L, 4L));
        assertTrue(optionalFriendship.isEmpty());
        optionalFriendship = friendshipRepository.save(new Friendship(3L, 1L, 5L));
        assertTrue(optionalFriendship.isPresent());
        assertThrows(ValidationException.class, () -> friendshipRepository.save(new Friendship(10L, 2L, 6L)));
    }

    @Test
    void delete() {
        Optional<Friendship> optionalFriendship = friendshipRepository.delete(new Tuple<>(3L, 2L));
        assertTrue(optionalFriendship.isPresent());
        assertEquals(optionalFriendship.get().getId().getRight(), 2L);
        assertEquals(optionalFriendship.get().getId().getLeft(), 3L);
        optionalFriendship = friendshipRepository.delete(new Tuple<>(10L, 1L));
        assertTrue(optionalFriendship.isEmpty());
        optionalFriendship = friendshipRepository.delete(new Tuple<>(2L, 3L));
        assertTrue(optionalFriendship.isEmpty());
    }

    @Test
    void update() {
        Optional<Friendship> optionalFriendship = friendshipRepository.update(new Friendship(1L, 3L, 1L));
        assertTrue(optionalFriendship.isEmpty());
        optionalFriendship = friendshipRepository.findOne(new Tuple<>(1L, 3L));
        assertTrue(optionalFriendship.isPresent());
        optionalFriendship = friendshipRepository.update(new Friendship(2L, 4L, 4L));
        assertTrue(optionalFriendship.isPresent());
        optionalFriendship = friendshipRepository.update(new Friendship(10L, 4L, 3L));
        assertTrue(optionalFriendship.isPresent());
        assertEquals(optionalFriendship.get().getId().getLeft(), 10L);
        assertEquals(optionalFriendship.get().getId().getRight(), 4L);
    }
}