package com.example.map223rebecadomocosmaragheorghe.service.DTO;

import com.example.map223rebecadomocosmaragheorghe.domain.User;

import java.time.LocalDateTime;

public class MessageDTO {
    String body;
    User from;
    LocalDateTime date;
    Long idChat;


    public MessageDTO(String body, User from, LocalDateTime date, Long idChat) {
        this.body = body;
        this.from = from;
        this.date = date;
        this.idChat = idChat;
    }

    public String getBody() {
        return body;
    }

    public User getFrom() {
        return from;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String toReport() {
        if (from == null || body == null)
            return "";
        else return from.getCompleteName() + " : " + body + "\n" + date.toString() + "\n";
    }

    public String downloadConversation() {
        if (from == null || body == null)
            return "";
        else return body + "  (" + date.toString() + ")\n";
    }

    @Override
    public String toString() {
        if (from == null || body == null)
            return "";
        else return from.getCompleteName() + " : " + body;
    }
}



