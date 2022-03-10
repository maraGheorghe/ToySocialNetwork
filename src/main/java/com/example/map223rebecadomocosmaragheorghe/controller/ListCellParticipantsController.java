package com.example.map223rebecadomocosmaragheorghe.controller;
import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class ListCellParticipantsController extends ListCell<User> {

    private SuperService superService;
    @FXML
    Label label;
    @FXML
    Button addButton;
    @FXML
    HBox hbox;
    @FXML
    Circle profilePicture;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if(user == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("participant-cell-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profilePicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(user.getPhotoUrl());
        profilePicture.setFill(new ImagePattern(image));
        label.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        label.setAlignment(Pos.CENTER);
        if(superService.friendRequestService.existsRequest(user.getId(), superService.user.getId()).isPresent() || user.getId().equals(superService.user.getId()))
            addButton.setVisible(false);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                superService.friendRequestService.addFriendRequest(superService.user.getId(), user.getId());
                addButton.setVisible(false);
            }
        });
        setText(null);
        setGraphic(hbox);
    }
}
