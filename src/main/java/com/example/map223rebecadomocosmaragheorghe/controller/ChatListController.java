package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.FriendshipDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.design.ComboBoxAutoComplete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class ChatListController implements Initializable {
    @FXML
    public Button newGroupButton;
    @FXML
    public ListView<UserDTO> listViewChats;
    @FXML
    public TextField searchBar;
    @FXML
    public ListView<ChatDTO> chatList;
    @FXML
    public AnchorPane rootPane;

    ObservableList<ChatDTO> model = FXCollections.observableArrayList();
    ObservableList<UserDTO> users = FXCollections.observableArrayList();

    private SuperService superService;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        setModel();
        setUsers();
        searchBar.textProperty().addListener(x -> {
            if (searchBar.getText().equals(""))
                listViewChats.setVisible(false);
            else {
                listViewChats.setVisible(true);
                initModelUsers(searchBar.getText());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewChats.setItems(users);
        chatList.setItems(model);
        chatList.setCellFactory(new Callback<ListView<ChatDTO>, ListCell<ChatDTO>>() {
            @Override
            public ListCell<ChatDTO> call(ListView<ChatDTO> param) {
                return new CellController();
            }
        });
        listViewChats.setCellFactory(x -> {
            UserCell cell = new UserCell();
            cell.setSuperService(superService);
            return cell;
        });
    }

    public void setModel() {
        List<ChatDTO> chats = new ArrayList<>();
        superService.chatService
                .findChatsOfUser(superService.getUser().getId())
                .forEach(chats::add);
        List<ChatDTO> sortedChats = chats.stream()
                .sorted((c1, c2) -> {
                    if (c1.getLastMessage().getDate() != null && c2.getLastMessage().getDate() != null)
                        return c2.getLastMessage().getDate().compareTo(c1.getLastMessage().getDate());
                    else return c2.getId().compareTo(c1.getId());
                })
                .collect(Collectors.toList());
        model.setAll(sortedChats);
    }

    @FXML
    public void handleNewGroupButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("createGroup-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<GroupController>getController().setSuperService(this.superService);

        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }

    private void setUsers() {
        listViewChats.setItems(users);
    }

    public void initModelUsers(String match) {
        List<User> matching = superService.userService.findMatching(match, superService.user.getId());
        var usersDtoModel = matching.stream().filter(x -> superService.friendRequestService.areFriends(superService.user.getId(), x.getId())).toList()
        .stream()
                .map(user -> new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhotoUrl()))
                .collect(Collectors.toList());
        users.setAll(usersDtoModel);
    }

    public void handleOpenOrCreateChat(MouseEvent mouseEvent) throws Exception {
        if(mouseEvent.getClickCount() == 1) {
            var user = listViewChats.getSelectionModel().getSelectedItem();
            if(user != null) {
                List<ChatDTO> chats = model.stream().filter(x -> x.getParticipants().contains(superService.userService.findOneByEmail(user.getEmail()).get()) && x.getParticipants().size() == 2).toList();
                if (chats.size() == 0) {
                    ChatDTO chat = new ChatDTO(user.getFullName(), new ArrayList<>(List.of(superService.userService.findOneByEmail(user.getEmail()).get(), superService.getUser())), user.getPicture());
                    superService.chatService.saveChat(chat);
                    searchBar.setText("");
                    setModel();
                }
                else {
                    FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chat-view.fxml"));
                    Parent parent = (Parent) fxmlLoader.load();
                    Long idChat = chats.get(0).getId();
                    fxmlLoader.<ChatController>getController().setIDChat(idChat);
                    fxmlLoader.<ChatController>getController().setSuperService(this.superService);
                    rootPane.getChildren().removeAll();
                    rootPane.getChildren().setAll(parent);
                }
            }
        }
    }

    @FXML
    public void handleOpenChat(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chat-view.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Long idChat;
            Optional<ChatDTO> chat = Optional.ofNullable(chatList.getSelectionModel().getSelectedItem());
            if (chat.isPresent()) {
                idChat = chatList.getSelectionModel().getSelectedItem().getId();
                fxmlLoader.<ChatController>getController().setIDChat(idChat);
                fxmlLoader.<ChatController>getController().setSuperService(this.superService);
            }
            rootPane.getChildren().removeAll();
            rootPane.getChildren().setAll(parent);
        }
    }
}
