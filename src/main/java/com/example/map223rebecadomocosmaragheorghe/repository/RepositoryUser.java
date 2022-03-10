package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.User;
import java.util.List;

import java.util.Optional;

public interface RepositoryUser extends Repository<Long, User> {
    Optional<User> findUserByEmail(String email);

    List<User> findMatching(String name, Long ID);
}
