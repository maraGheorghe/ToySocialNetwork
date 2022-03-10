package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Optional;

public class ListCellEventController extends ListCell<Event> {
    private SuperService superService;
    @FXML
    Rectangle eventPicture;
    @FXML
    Label labelName;
    @FXML
    Label labelOrganizer;
    @FXML
    Label labelDescription;
    @FXML
    Label labelDate;
    @FXML
    Button participateButton;
    @FXML
    Button cancelButton;
    @FXML
    ToggleButton notificationButton;
    @FXML
    HBox hBox;
    @FXML
    ImageView imageView;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);
        if(event == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("listCellEvents-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        eventPicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(event.getPhotoUrl());
        eventPicture.setFill(new ImagePattern(image));
        labelOrganizer.setText("Organized by: " + event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName() + " (" + event.getOrganizer().getEmail() + ")");
        labelOrganizer.setAlignment(Pos.CENTER);
        labelName.setText(event.getName());
        labelName.setAlignment(Pos.CENTER);
        labelDate.setText("Date: " + event.getDate().format(Constants.DATE_TIME_FORMATTER));
        labelDate.setAlignment(Pos.CENTER);
        User user = superService.getUser();
        if(event.getParticipants().contains(superService.getUser())) {
            participateButton.setVisible(false);
            notificationButton.setVisible(true);
            if(superService.eventService.getNotificationStatus(user.getId(), event.getId())) {
                Image imageNotification = new Image(StartApplication.class.getResource("/images/reminderON.png").toExternalForm());
                imageView.setImage(imageNotification);
                notificationButton.setSelected(false);
            }
            else {
                Image imageNotification = new Image(StartApplication.class.getResource("/images/reminderOFF.png").toExternalForm());
                imageView.setImage(imageNotification);
                notificationButton.setSelected(true);
            }
        }
        else {
            cancelButton.setVisible(false);
            cancelButton.setVisible(false);
            notificationButton.setVisible(false);
        }
        participateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                participateButton.setVisible(false);
                cancelButton.setVisible(true);
                notificationButton.setVisible(true);
                superService.eventService.addParticipant(user.getId(), event.getId());
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cancelButton.setVisible(false);
                participateButton.setVisible(true);
                notificationButton.setVisible(false);
                superService.eventService.cancelParticipation(user.getId(), event.getId());
            }
        });
        notificationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(superService.eventService.getNotificationStatus(user.getId(), event.getId())) {
                    superService.eventService.changeNotificationsStatus(user.getId(), event.getId(), false);
                    Image image = new Image(StartApplication.class.getResource("/images/reminderOFF.png").toExternalForm());
                    imageView.setImage(image);
                }
                else {
                    superService.eventService.changeNotificationsStatus(user.getId(), event.getId(), true);
                    Image image = new Image(StartApplication.class.getResource("/images/reminderON.png").toExternalForm());
                    imageView.setImage(image);
                }
            }
        });
        setText(null);
        setGraphic(hBox);
    }
}
