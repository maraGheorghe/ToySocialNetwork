package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.*;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

public class ListCellNotificationController extends ListCell<Notification> {
    private SuperService superService;
    @FXML
    Label labelDescription;
    @FXML
    AnchorPane anchorPane;
    @FXML
    ImageView imageView;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @Override
    protected void updateItem(Notification notification, boolean empty) {
        super.updateItem(notification, empty);
        if(notification == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("listCellNotification.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        labelDescription.setWrapText(true);
        if(notification.getType().equals(NotificationType.EventNotification)) {
            Image image = new Image(StartApplication.class.getResource("/images/notification_event.png").toExternalForm());
            imageView.setImage(image);
            var days = DAYS.between(LocalDateTime.now(), ((Event) notification.getEntity()).getDate());
            if(days == 0)
                labelDescription.setText("UPCOMING EVENT: " + ((Event) notification.getEntity()).getName() + " today.");
            else labelDescription.setText("UPCOMING EVENT: " + ((Event) notification.getEntity()).getName() + " in " + days + " days.");
        }
        else if(notification.getType().equals(NotificationType.RequestNotification)) {
            Image image = new Image(StartApplication.class.getResource("/images/addFriend.png").toExternalForm());
            imageView.setImage(image);
            var friendID = ((FriendRequest) notification.getEntity()).getFrom();
            var friend = superService.userService.findOneUser(friendID);
            labelDescription.setText("You received a request from " + friend.get().getCompleteName() + ".");
        }
        else if(notification.getType().equals(NotificationType.MessageNotification)) {
            Image image = new Image(StartApplication.class.getResource("/images/chatNotification.png").toExternalForm());
            imageView.setImage(image);
            labelDescription.setText("Active chat: " + ((Chat) notification.getEntity()).getName() + ".");
        }
        setText(null);
        setGraphic(anchorPane);
    }
}
