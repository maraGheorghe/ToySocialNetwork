package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class ListCellCancelRequestController extends ListCell<FriendRequestDTO> {

    private SuperService superService;
    @FXML
    Circle profilePictureCircle;
    @FXML
    Label labelText;
    @FXML
    Button cancelButton;
    @FXML
    HBox hBox;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @Override
    protected void updateItem(FriendRequestDTO friendRequestDTO, boolean empty) {
        super.updateItem(friendRequestDTO, empty);
        if(friendRequestDTO == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("listCellRequestsByMe-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image = new Image(friendRequestDTO.getPhotoUrl());
        profilePictureCircle.setFill(new ImagePattern(image));
        labelText.setText("You sent a friend request to " + friendRequestDTO.getFirstName()
                + " " + friendRequestDTO.getLastName() + " (" + friendRequestDTO.getUsername() + ").");
        cancelButton.setOnAction(event -> superService.friendRequestService.deleteFriendRequest(friendRequestDTO.getRequestID()));
        setText(null);
        setGraphic(hBox);
    }
}
