package com.example.map223rebecadomocosmaragheorghe.repository;

import com.example.map223rebecadomocosmaragheorghe.domain.Friendship;
import com.example.map223rebecadomocosmaragheorghe.domain.Tuple;
import java.util.List;

public interface RepositoryFriendship extends Repository<Tuple<Long>, Friendship> {

    List <Friendship> getAllFriendshipsForAUser(Long userID);
}
