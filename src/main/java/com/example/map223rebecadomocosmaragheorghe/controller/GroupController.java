package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendshipDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.stream.Collectors;

public class GroupController implements Initializable {
    @FXML
    public AnchorPane groupPane;
    @FXML
    public ListView<UserDTO> profileList;
    @FXML
    public Button nextButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label invalidLabel;

    ObservableList<UserDTO> profileModel = FXCollections.observableArrayList();
    ListView<UserDTO> selectedProfiles = new ListView<>();

    private SuperService superService;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        setModel();
    }

    private void setModel() {
        List<UserDTO> users = new ArrayList<>();
        superService.communityService
                .getAllFriendshipsOfAUser(superService.getUser().getId(),superService.communityService.getFriendsOfAUser(superService.getUser().getId()))
                .stream()
                .map(FriendshipDTO::getUserDto)
                .forEach(users::add);
        profileModel.setAll(users);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profileList.setItems(profileModel);
        profileList.setCellFactory(x -> {
            var cell = new GroupParticipantsController();
            cell.setSuperService(superService);
            return cell;
        });
        profileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        profileList.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            selectedProfiles.setItems(profileList.getSelectionModel().getSelectedItems());
        });
        profileList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();
            while (node != null && node != profileList && !(node instanceof ListCell)) {
                node = node.getParent();
            }
            if (node instanceof ListCell) {
                evt.consume();
                ListCell cell = (ListCell) node;
                ListView listView = cell.getListView();
                listView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (cell.isSelected()) {
                        listView.getSelectionModel().clearSelection(index);
                    } else {
                        listView.getSelectionModel().select(index);
                    }
                }
            }
        });
    }

    @FXML
    public void handleNextButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("next-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        List<UserDTO> participants = new ArrayList<UserDTO>(new ArrayList<>(selectedProfiles.getItems()));
        if(participants.size() < 2) {
            invalidLabel.setVisible(true);
            return;
        }
        else {
            UserDTO userDTO = superService.getUser().toDTO();
            participants.add(userDTO);
            fxmlLoader.<ConfigurationController>getController().setParticipants(participants);
            fxmlLoader.<ConfigurationController>getController().setSuperService(this.superService);
        }
        groupPane.getChildren().removeAll();
        groupPane.getChildren().setAll(parent);
    }

    @FXML
    public void handleCancelButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chatList-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ChatListController>getController().setSuperService(this.superService);
        groupPane.getChildren().removeAll();
        groupPane.getChildren().setAll(parent);
    }
}
