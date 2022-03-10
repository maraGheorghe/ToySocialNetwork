package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendshipChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AllFriendsController implements Initializable, Observer<FriendshipChangeEvent> {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button backButton;
    @FXML
    public TextField searchBar;
    @FXML
    public ListView<UserDTO> profileList;
    @FXML
    public AnchorPane friendProfile;

    private ObservableList<UserDTO> modelUsers = FXCollections.observableArrayList();
    private SuperService superService;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        this.superService.friendshipService.addObserver(this);
        setModelUsers();
        searchBar.textProperty().addListener(x -> {
            if (searchBar.getText().equals(""))
                setModelUsers();
            else {
                handleSearchBar();
            }
        });
    }

    private List<UserDTO> getAllUsersDTO() {
        List<UserDTO> usersMain = new ArrayList<>();
        superService.communityService.getAllFriendshipsOfAUser(superService.getUser().getId(), superService.communityService.getFriendsOfAUser(superService.user.getId())).
                forEach(friendshipDto -> usersMain.add(friendshipDto.getUserDto()));
        return usersMain;
    }

    private void setModelUsers(){
        modelUsers.setAll(getAllUsersDTO());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profileList.setItems(modelUsers);
        profileList.setCellFactory(new Callback<ListView<UserDTO>, ListCell<UserDTO>>() {
            @Override
            public ListCell<UserDTO> call(ListView<UserDTO> param) {
                ListCellProfileDelete listCellProfileDelete =  new ListCellProfileDelete();
                listCellProfileDelete.setSuperService(superService);
                return listCellProfileDelete;
            }
        });
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        EditProfileController.goBackToProfile(backButton, this.superService, rootPane);
    }

    @FXML
    public void handleSearchBar() {
        Predicate<UserDTO> p = userDto -> {
            return userDto.getFirstName().startsWith(searchBar.getText()) || userDto.getLastName().startsWith(searchBar.getText());
        };
        modelUsers.setAll(getAllUsersDTO().stream().filter(p).collect(Collectors.toList()));
    }

    public void showFriendsProfile(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            Optional<UserDTO> user = Optional.ofNullable(profileList.getSelectionModel().getSelectedItem());
            if (user.isPresent()) {
                friendProfile.setVisible(true);
                FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("friendProfile-view.fxml"));
                Parent parent = fxmlLoader.load();
                fxmlLoader.<FriendsProfileController>getController().setSuperService(superService);
                fxmlLoader.<FriendsProfileController>getController().setProfile(user.get());
                friendProfile.getChildren().removeAll();
                friendProfile.getChildren().setAll(parent);
            }
            else
                friendProfile.setVisible(false);
        }
        else friendProfile.setVisible(false);
    }

    public void exitFriendsProfile(MouseEvent mouseEvent) {
        friendProfile.setVisible(false);
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        setModelUsers();
    }
}
