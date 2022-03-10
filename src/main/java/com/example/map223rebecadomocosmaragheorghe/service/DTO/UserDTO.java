package com.example.map223rebecadomocosmaragheorghe.service.DTO;
public class UserDTO {
    String firstName;
    String lastName;
    String email;
    String picture;

    public UserDTO(String firstName, String lastName, String email, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getEmail(){return email;}

    public String getFirstName(){return firstName;}

    public String getLastName(){return lastName;}

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullName(){return firstName + " " + lastName;}

}
