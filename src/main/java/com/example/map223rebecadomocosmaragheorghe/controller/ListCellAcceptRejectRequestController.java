package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Status;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class ListCellAcceptRejectRequestController extends ListCell<FriendRequestDTO> {
    private SuperService superService;
    @FXML
    Circle profilePictureCircle;
    @FXML
    Label labelText;
    @FXML
    Button acceptButton;
    @FXML
    Button rejectButton;
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
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("listCellRequestsByOthers-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ignored) {}
        Image image = new Image(friendRequestDTO.getPhotoUrl());
        profilePictureCircle.setFill(new ImagePattern(image));
        labelText.setText(friendRequestDTO.getFirstName() + " " + friendRequestDTO.getLastName()
                + " (" + friendRequestDTO.getUsername() + ") sent you a friend request on " + friendRequestDTO.getDate() + ".");
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     superService.friendRequestService.updateFriendRequest(friendRequestDTO.getRequestID(), Status.approved);
                 }
        });
        rejectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    superService.friendRequestService.updateFriendRequest(friendRequestDTO.getRequestID(), Status.rejected);
                }
        });
        setText(null);
        setGraphic(hBox);
    }
}
