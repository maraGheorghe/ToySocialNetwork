package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendRequestDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.events.FriendRequestChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class RequestsController implements Observer<FriendRequestChangeEvent> {
    private SuperService superService;
    private ObservableList<FriendRequestDTO> modelByMe = FXCollections.observableArrayList();
    private ObservableList<FriendRequestDTO> modelByOthers = FXCollections.observableArrayList();
    private ObservableList<FriendRequestDTO> modelHistory = FXCollections.observableArrayList();
    private ObservableList<User> modelUsers = FXCollections.observableArrayList();
    @FXML
    ListView<FriendRequestDTO> listView;
    @FXML
    ListView<User> listViewFriends;
    @FXML
    Button historyButton;
    @FXML
    Button backButton;
    @FXML
    ToggleButton toggleButton;
    @FXML
    Label invisibleLabel;
    @FXML
    TextField searchBar;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        initModelSentByMe();
        initModelSentByOthers();
        initModelHistory();
        this.superService.friendRequestService.addObserver(this);
        searchBar.textProperty().addListener(x -> {
            if (searchBar.getText().equals(""))
                listViewFriends.setVisible(false);
            else {
                listViewFriends.setVisible(true);
                initModelUsers(searchBar.getText());
            }
        });
    }

    public void initialize() {
        setRequestsSentByOthers();
        setModelUsers();
    }

    public void setRequestsSentByMe() {
        listView.setItems(modelByMe);
        listView.setCellFactory(x -> {
            ListCellCancelRequestController cell = new ListCellCancelRequestController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    public void setRequestsSentByOthers() {
        listView.setItems(modelByOthers);
        listView.setCellFactory(x -> {
            ListCellAcceptRejectRequestController cell = new ListCellAcceptRejectRequestController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    public void setHistory () {
        listView.setItems(modelHistory);
        listView.setCellFactory(x -> {
            ListCellHistoryRequestController cell = new ListCellHistoryRequestController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    public void initModelSentByMe() {
        Iterable<FriendRequestDTO> friendRequests = superService.friendRequestService.getRequestsSentByOneUser(superService.getUser().getId());
        List<FriendRequestDTO> requestsSentList = new ArrayList<>();
        friendRequests.forEach(f -> {
            if(f.getStatus().equals("pending"))
                requestsSentList.add(f);
        });
        modelByMe.setAll(requestsSentList);
    }

    public void initModelSentByOthers() {
        Iterable<FriendRequestDTO> friendRequests = superService.friendRequestService.getRequestsForOneUser(superService.user.getId());
        List<FriendRequestDTO> friendRequestList = new ArrayList<>();
        friendRequests.forEach(f -> {
            if(f.getStatus().equals("pending"))
                friendRequestList.add(f);
        });
        modelByOthers.setAll(friendRequestList);
    }

    public void initModelHistory() {
        Iterable<FriendRequestDTO> friendRequests = superService.friendRequestService.getRequestsForOneUser(superService.getUser().getId());
        List<FriendRequestDTO> friendRequestList = new ArrayList<>();
        friendRequests.forEach(f -> {
            if(!f.getStatus().equals("pending"))
                friendRequestList.add(f);
        });
        modelHistory.setAll(friendRequestList);
    }

    public void handleToggleButton() {
        if(toggleButton.isSelected()) {
            toggleButton.setText("Sent to me");
            setRequestsSentByMe();
        }
        else {
            toggleButton.setText("Sent by me");
            setRequestsSentByOthers();
        }
    }

    public void handleHistoryButton(ActionEvent actionEvent) {
        setHistory();
        toggleButton.setVisible(false);
        historyButton.setVisible(false);
        backButton.setVisible(true);
        listView.setFocusTraversable(false);
    }

    public void handleBackButton(ActionEvent actionEvent) {
        toggleButton.setVisible(true);
        historyButton.setVisible(true);
        backButton.setVisible(false);
        listView.setFocusTraversable(true);
        handleToggleButton();
    }

    public void setModelUsers() {
        listViewFriends.setItems(modelUsers);
        listViewFriends.setCellFactory(x -> {
            ListCellParticipantsController cell = new ListCellParticipantsController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    public void initModelUsers(String match) {
        modelUsers.setAll(superService.userService.findMatching(match, superService.user.getId()));
        listViewFriends.setItems(modelUsers);
    }

    @Override
    public void update(FriendRequestChangeEvent friendRequestChangeEvent) {
        initModelSentByOthers();
        initModelSentByMe();
        initModelHistory();
    }
}