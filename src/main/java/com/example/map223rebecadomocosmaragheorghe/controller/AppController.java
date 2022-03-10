package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class AppController implements PropertyChangeListener {
    @FXML
    public Pane pane;
    @FXML
    public Pane notificationPane;
    @FXML
    public Button friendRequestsButton;
    @FXML
    public Button profileButton;
    @FXML
    public Button eventButton;
    @FXML
    public Button logOutButton;
    @FXML
    public Button messagesButton;
    @FXML
    public ToggleButton notificationButton;
    @FXML
    public Label label;
    @FXML
    AnchorPane eventsCircle;
    @FXML
    AnchorPane requestsCircle;
    @FXML
    Label notificationsRequestsLabel;
    @FXML
    Label notificationsEventsLabel;
    @FXML
    AnchorPane rootPaneMain;
    @FXML
    private Stage notificationStage = new Stage();


    private SuperService superService;

    @FXML
    public void setSuperService(SuperService superService) throws IOException {
        this.superService = superService;
        superService.setMainPane(this.rootPaneMain);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("profile-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ProfileController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
        label.setText("Hello, " + superService.user.getCompleteName() + " !");
        initializeNotificationsEvents();
        initializeNotificationsRequests();
        notificationStage.initStyle(StageStyle.UNDECORATED);
        this.superService.addPropertyChangeListener(this);
    }

    public void initializeNotificationsEvents() {
        Integer noEvents = superService.getPage().getNumberOfEvents();
        if (noEvents == 0)
            eventsCircle.setVisible(false);
        else {
            eventsCircle.setVisible(true);
            notificationsEventsLabel.setText(noEvents.toString());
        }
    }

    public void initializeNotificationsRequests() {
        Integer noRequests = superService.getPage().getNumberOfRequests();
        if (noRequests == 0)
            requestsCircle.setVisible(false);
        else {
            requestsCircle.setVisible(true);
            notificationsRequestsLabel.setText(noRequests.toString());
        }
    }

    @FXML
    public void handleEventButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("events-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<EventsController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
    }

    @FXML
    public void handleProfileButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("profile-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ProfileController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
    }

    @FXML
    public void handleFriendRequestButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("requests-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<RequestsController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
    }

    @FXML
    protected void handleLogOutButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        superService.setUser(null);
        fxmlLoader.<LoginController>getController().setSuperService(superService);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void handleMessagesButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chatList-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ChatListController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
    }

    @FXML
    public void handleShowNotification(ActionEvent actionEvent) throws IOException {
        if (notificationButton.isSelected()) {
            notificationPane.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("notifications-view.fxml"));
            Parent parent = fxmlLoader.load();
            fxmlLoader.<NotificationController>getController().setSuperService(superService);
            notificationPane.getChildren().removeAll();
            notificationPane.getChildren().setAll(parent);
        } else notificationPane.setVisible(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initializeNotificationsEvents();
        initializeNotificationsRequests();
    }

    @FXML
    public void closeNotification(MouseEvent mouseEvent) {
        notificationPane.setVisible(false);
        notificationButton.setSelected(false);
    }
}
