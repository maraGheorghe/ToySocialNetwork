package com.example.map223rebecadomocosmaragheorghe.service.DTO;

import java.time.LocalDate;

public class FriendRequestDTO {
    private Long requestID;
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private LocalDate date;
    private String photoUrl;

    public FriendRequestDTO(Long requestID, String username, String firstName, String lastName, String photoUrl, String status, LocalDate date) {
        this.requestID = requestID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.date = date;
        this.photoUrl = photoUrl;
    }

    public Long getRequestID() {
        return requestID;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String toString() {
        return "FriendRequestDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
