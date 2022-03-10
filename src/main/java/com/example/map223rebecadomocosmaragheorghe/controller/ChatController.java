package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.Message;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.MessageDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.events.MessageChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChatController implements Initializable, Observer<MessageChangeEvent> {
    @FXML
    public AnchorPane pane;
    @FXML
    public Label chatName;
    @FXML
    public Button backButton;
    @FXML
    public TextField textInput;
    @FXML
    public Button sendButton;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public ListView<MessageDTO> messageList;
    @FXML
    public AnchorPane replyPane;
    @FXML
    public Label replyLabel;
    public String reply = "";

    ObservableList<MessageDTO> model = FXCollections.observableArrayList();

    private SuperService superService;
    private Long IDChat;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        this.superService.chatService.addObserver(this);
        this.superService.messageService.addObserver(this);
        setModel();
        chatName.setText(superService.chatService.findOne(IDChat).get().getName());
        messageList.scrollTo(messageList.getItems().size() - 1);
        Chat chat = superService.chatService.findOne(IDChat).get();
        if ( !(chat.getName() == null))
            chatName.setText(superService.chatService.findOne(IDChat).get().getName());
        else {
            var participants = chat.getParticipants();
            var chatNameAsList = participants
                    .stream()
                    .filter(user -> {
                        return user.getId() != superService.user.getId();
                    })
                    .collect(Collectors.toList());
            System.out.println(chatNameAsList);
            if (chatNameAsList.size() == 1) {
                System.out.println(chatNameAsList.get(0).getCompleteName());
                chatName.setText(chatNameAsList.get(0).getCompleteName());
            }
            else {
                String name = "";
                chatNameAsList.stream()
                        .map(User::getCompleteName)
                        .collect(Collectors.toList())
                        .forEach(username -> name.concat(username).concat(", "));
                chatName.setText(name);
            }
        }
    }

    public void setIDChat(Long IDChat) {
        this.IDChat = IDChat;
    }

    private void setModel() {
        List<MessageDTO> messages = new ArrayList<>(superService.chatService.findMessagesFromChat(IDChat));
        model.setAll(messages.stream()
                .sorted(Comparator.comparing(MessageDTO::getDate))
                .collect(Collectors.toList()));
        messageList.scrollTo(messageList.getItems().size() - 1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageList.setItems(model);
        messageList.getItems().addListener((ListChangeListener<MessageDTO>) change -> {
            while (change.next()) {
                messageList.scrollTo(messageList.getItems().size() - 1);
            }
        });
        messageList.setCellFactory(new Callback<ListView<MessageDTO>, ListCell<MessageDTO>>() {
            @Override
            public ListCell<MessageDTO> call(ListView<MessageDTO> param) {
                MyMessageController myMessageController = new MyMessageController();
                myMessageController.setSuperService(superService);
                return myMessageController;
            }
        });
    }

    @FXML
    public void handleReply(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            var message = messageList.getSelectionModel().getSelectedItem();
            reply = "Replied to: " + message.getFrom().getCompleteName() + " -> " + message.getBody();
            replyPane.setVisible(true);
            replyLabel.setText(reply);
        }
    }

    @FXML
    public void handleSendButton(ActionEvent actionEvent) {
        String messageToSend = "";
        if(!reply.equals(""))
            messageToSend += reply + "\nResponse: ";
        reply = "";
        messageToSend += textInput.getText();
        if (!messageToSend.isEmpty()) {
            User from = superService.getUser();
            Message message = new Message(from, messageToSend, LocalDateTime.now(), IDChat);
            superService.messageService.saveMessage(message);
            textInput.clear();
            replyLabel.setText("");
            replyPane.setVisible(false);
        }
    }

    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chatList-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ChatListController>getController().setSuperService(this.superService);
        pane.getChildren().removeAll();
        pane.getChildren().setAll(parent);
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        setModel();
    }
}