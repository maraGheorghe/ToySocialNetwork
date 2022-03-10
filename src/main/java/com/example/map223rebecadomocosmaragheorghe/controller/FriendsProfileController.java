package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.stream.StreamSupport;

public class FriendsProfileController {
    @FXML
    public Circle circleProfilePicture;
    @FXML
    public Label nameLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label friendsLabel;
    @FXML
    public Label eventsLabel;
    @FXML
    public AnchorPane friendProfilePane;

    private SuperService superService;
    private User user;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        this.user = superService.getUser();
    }

    public void setCircleProfilePicture(UserDTO hoveredUser) {
        this.circleProfilePicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(hoveredUser.getPicture());
        this.circleProfilePicture.setFill(new ImagePattern(image));
    }

    public void setNameLabel(UserDTO hoveredUser) {
        this.nameLabel.setText(hoveredUser.getFullName());
    }

    public void setEmailLabel(UserDTO hoveredUser) {
        this.emailLabel.setText(hoveredUser.getEmail());
    }

    public void setFriendsLabel() {
        this.friendsLabel.setText(String.valueOf(superService.communityService.getAllFriendshipsOfAUser(user.getId(), superService.communityService.getFriendsOfAUser(user.getId())).size()));
    }

    public void setEventsLabel() {
        this.eventsLabel.setText(String.valueOf(StreamSupport
                .stream(superService.eventService.getEventsForAUser(user.getId()).spliterator(), false)
                .count()));
    }

    public AnchorPane getFriendProfilePane() {
        return this.friendProfilePane;
    }

    public void setProfile(UserDTO hoveredUser) {
        this.user = superService.userService.findOneByEmail(hoveredUser.getEmail()).get();
        setCircleProfilePicture(hoveredUser);
        setFriendsLabel();
        setNameLabel(hoveredUser);
        setEmailLabel(hoveredUser);
        setEventsLabel();
    }
}
