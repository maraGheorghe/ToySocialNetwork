package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendshipChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;
import com.example.map223rebecadomocosmaragheorghe.utils.reports.ReportsType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfileController implements Initializable, Observer<FriendshipChangeEvent> {
    @FXML
    public Label nameLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label friendsLabel;
    @FXML
    public Label eventsLabel;
    @FXML
    public ListView<UserDTO> friendList;
    @FXML
    public Button editButton;
    @FXML
    public Circle circleProfilePicture;
    @FXML
    public ComboBox<String> reportsBox;
    @FXML
    public Button seeFriendsButton;
    @FXML
    public ListView<UserDTO> friendList1;
    @FXML
    public ListView<UserDTO> friendList11;
    @FXML
    public Pane rootPane;

    ObservableList<UserDTO> model = FXCollections.observableArrayList();
    ObservableList<UserDTO> model1 = FXCollections.observableArrayList();
    ObservableList<UserDTO> model11 = FXCollections.observableArrayList();
    ObservableList<String> reports = FXCollections.observableArrayList( "my activity", "messages from a user");
    private SuperService superService;
    private User user;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        this.superService.friendshipService.addObserver(this);
        this.user = superService.getUser();
        setProfile();
        setModel();
    }

    public void setProfile() {
        circleProfilePicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(superService.user.getPhotoUrl());
        circleProfilePicture.setFill(new ImagePattern(image));
        this.nameLabel.setText(user.getCompleteName());
        this.emailLabel.setText(user.getEmail());
        String no = String.valueOf(superService.communityService.getAllFriendshipsOfAUser(user.getId(), superService.communityService.getFriendsOfAUser(user.getId())).size());
        this.friendsLabel.setText(no);
        this.eventsLabel.setText(String.valueOf(StreamSupport
                .stream(superService.eventService.getEventsForAUser(user.getId()).spliterator(), false)
                .count()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendList.setItems(model);
        friendList1.setItems(model1);
        friendList11.setItems(model11);
        setFactory(friendList);
        setFactory(friendList1);
        setFactory(friendList11);
        reportsBox.setItems(reports);
        reportsBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (reportsBox.getValue().equals("my activity")) {
                    showDatePicker(ReportsType.ACTIVITY);
                }
                if (reportsBox.getValue().equals("messages from a user")) {
                    showDatePicker(ReportsType.MESSAGES);
                }
            }
        });
    }

    private void showDatePicker(ReportsType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/com/example/map223rebecadomocosmaragheorghe/datepicker-view.fxml"));
        AnchorPane root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fxmlLoader.<DatePickerController>getController().setSuperService(superService);
        fxmlLoader.<DatePickerController>getController().setReportsType(type);
        Scene scene = new Scene(root);
        Stage secondStage = new Stage();
        secondStage.getIcons().add(new Image("/images/peanuts.png"));
        secondStage.setTitle("Pick a time period");
        secondStage.setScene(scene);
        secondStage.show();
    }

    private void setFactory(ListView<UserDTO> friendList) {
        friendList.setCellFactory(new Callback<ListView<UserDTO>, ListCell<UserDTO>>() {
            @Override
            public ListCell<UserDTO> call(ListView<UserDTO> param) {
                ProfileCellController profileCellController = new ProfileCellController();
                profileCellController.setService(superService);
                return profileCellController;
            }
        });
    }

    private void setModel() {
        List<UserDTO> usersMain = new ArrayList<>();
        List<UserDTO> users = new ArrayList<>();
        List<UserDTO> users1 = new ArrayList<>();
        List<UserDTO> users11 = new ArrayList<>();
        superService.communityService.getAllFriendshipsOfAUser(superService.getUser().getId(), superService.communityService.getFriendsOfAUser(superService.user.getId())).
                forEach(friendshipDto -> usersMain.add(friendshipDto.getUserDto()));
        int index = 0;
        for (UserDTO userDTO : usersMain) {
            if (index == 0 || index == 3) users.add(userDTO);
            if (index == 1 || index == 4) users1.add(userDTO);
            if (index == 2 || index == 5) users11.add(userDTO);
            index++;
        }
        model.setAll(users.stream().limit(2L).collect(Collectors.toList()));
        model1.setAll(users1.stream().limit(2L).collect(Collectors.toList()));
        model11.setAll(users11.stream().limit(2L).collect(Collectors.toList()));
    }

    public void handleEditButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/com/example/map223rebecadomocosmaragheorghe/editProfile-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<EditProfileController>getController().setSuperService(this.superService);
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }

    public void handleSeeFriendsButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/com/example/map223rebecadomocosmaragheorghe/allFriends-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<AllFriendsController>getController().setSuperService(this.superService);
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        setModel();
        setProfile();
    }
}
