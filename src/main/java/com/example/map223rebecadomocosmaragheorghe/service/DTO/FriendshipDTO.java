package com.example.map223rebecadomocosmaragheorghe.service.DTO;
import java.time.LocalDate;

public class FriendshipDTO {
    UserDTO user;
    LocalDate date;

    public FriendshipDTO(UserDTO user, LocalDate date) {
        this.user = user;
        this.date = date;
    }

    @Override
    public String toString() {
        return user.toString() + " | " + date.toString();
    }

    public UserDTO getUserDto(){return user;}
}
