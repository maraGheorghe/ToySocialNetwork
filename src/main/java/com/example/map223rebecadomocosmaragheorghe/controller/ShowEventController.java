package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowEventController {

    @FXML
    ListView<User> listView;
    @FXML
    Label labelName;
    @FXML
    Label labelDescription;
    @FXML
    Label labelDate;
    @FXML
    Label labelOrganizer;
    @FXML
    Label labelParticipants;
    @FXML
    Button backButton;
    @FXML
    Button participateButton;
    @FXML
    Button cancelButton;
    @FXML
    ToggleButton notificationButton;
    @FXML
    Rectangle eventPicture;
    @FXML
    Pane rootPane;
    @FXML
    ImageView imageView;
    Event event;

    ObservableList<User> model = FXCollections.observableArrayList();

    private SuperService superService;
    private Long IDEvent;

    public void setIDEvent(Long IDEvent) {
        this.IDEvent = IDEvent;
    }

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        event = this.superService.eventService.findEvent(IDEvent).get();
        this.labelName.setText(event.getName());
        this.labelDescription.setText(event.getDescription());
        this.labelDate.setText("Date:\n " + event.getDate().format(Constants.DATE_TIME_FORMATTER));
        this.labelOrganizer.setText("Organized by:\n " + event.getOrganizer().getEmail());
        this.labelParticipants.setText(event.getParticipants().size() + " participants");
        eventPicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(event.getPhotoUrl());
        eventPicture.setFill(new ImagePattern(image));
        eventPicture.setEffect(new DropShadow(+10d, 0d, +2d, Color.valueOf("#B17A50B2")));
        if(event.getParticipants().contains(superService.getUser())) {
            if(superService.eventService.getNotificationStatus(superService.user.getId(), event.getId())) {
                Image imageNotification = new Image(StartApplication.class.getResource("/images/reminderON.png").toExternalForm());
                imageView.setImage(imageNotification);
                notificationButton.setSelected(false);
            }
            else {
                Image imageNotification = new Image(StartApplication.class.getResource("/images/reminderOFF.png").toExternalForm());
                imageView.setImage(imageNotification);
                notificationButton.setSelected(true);
            }
            notificationButton.setVisible(true);
            participateButton.setVisible(false);
        }
        else {
            cancelButton.setVisible(false);
            notificationButton.setVisible(false);
        }
        initModel();
    }

    public void initialize () {
        listView.setItems(model);
        listView.setCellFactory(x -> {
            ListCellParticipantsController cell = new ListCellParticipantsController();
            cell.setSuperService(superService);
           return cell;
        });
    }

    public void initModel() {
        Iterable<User> participants = superService.eventService.getParticipantsForAnEvent(this.IDEvent);
        List<User> participantsDTO  = new ArrayList<>();
        participants.forEach(participantsDTO::add);
        model.setAll(participantsDTO);
    }

    public void handleBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("events-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<EventsController>getController().setSuperService(this.superService);
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }

    public void handleParticipateButton() {
        participateButton.setVisible(false);
        cancelButton.setVisible(true);
        notificationButton.setVisible(true);
        Image image = new Image(StartApplication.class.getResource("/images/reminderON.png").toExternalForm());
        imageView.setImage(image);
        superService.eventService.addParticipant(superService.getUser().getId(), IDEvent);
        superService.eventService.changeNotificationsStatus(superService.user.getId(), event.getId(), true);
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        cancelButton.setVisible(false);
        participateButton.setVisible(true);
        notificationButton.setVisible(false);
        superService.eventService.cancelParticipation(superService.getUser().getId(), IDEvent);
    }

    public void handleNotificationButton() {
        if(notificationButton.isSelected()) {
            Image image = new Image(StartApplication.class.getResource("/images/reminderOFF.png").toExternalForm());
            imageView.setImage(image);
            superService.eventService.changeNotificationsStatus(superService.user.getId(), event.getId(), false);
        }
        else {
            Image image = new Image(StartApplication.class.getResource("/images/reminderON.png").toExternalForm());
            imageView.setImage(image);
            superService.eventService.changeNotificationsStatus(superService.user.getId(), event.getId(), true);
        }
    }

}
