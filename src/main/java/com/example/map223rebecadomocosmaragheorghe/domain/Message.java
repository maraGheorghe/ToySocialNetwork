package com.example.map223rebecadomocosmaragheorghe.domain;
import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private User from;
    private String body;
    private LocalDateTime date;
    private Long idChat;

    public Message(User from, String message, LocalDateTime date, Long idChat) {
        this.from = from;
        this.body = message;
        this.date = date;
        this.idChat = idChat;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {this.date = date;}

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", idChat=" + idChat +
                '}';
    }
}
