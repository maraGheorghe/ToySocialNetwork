package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Chat;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConfigurationController {
    @FXML
    public AnchorPane configurationPane;
    @FXML
    public ImageView imageView;
    @FXML
    public Rectangle rectangleView;
    @FXML
    public Button chooseButton;
    @FXML
    public TextField nameField;
    @FXML
    public Button finishButton;

    private SuperService superService;
    private List<UserDTO> participants;
    private String photoUrl = Constants.groupPhoto;

    public void setParticipants(List<UserDTO> participants) {
        this.participants = participants;
    }

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        rectangleView.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(photoUrl);
        rectangleView.setFill(new ImagePattern(image));
        rectangleView.setEffect(new DropShadow(+10d, 0d, +2d, Color.valueOf("#B17A50B2")));
    }

    public void handleChooseButton(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a photo...");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            this.photoUrl = file.toURI().toString();
            Image image = new Image(photoUrl);
            rectangleView.setFill(new ImagePattern(image));
        }
    }

    public void handleFinishButton(ActionEvent actionEvent) throws IOException {
        StringBuilder chatName = new StringBuilder(nameField.getText());
        List<User> users = new ArrayList<>();
        participants.stream()
                .map(participant -> superService.userService.findOneByEmail(participant.getEmail()))
                .forEach(user -> users.add(user.get()));
        if (chatName.toString().equals(""))
            for (User user : users)
                chatName.append(user.getCompleteName()).append(" ");
        ChatDTO chatDTO = new ChatDTO(chatName.toString(), users, photoUrl);
        try {
            superService.chatService.saveChat(chatDTO);
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("chat-view.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            String finalChatName = chatName.toString();
            Chat chat = StreamSupport.stream(superService.chatService.findAll().spliterator(), false)
                    .filter(chat1 -> chat1.getName().equals(finalChatName) && chat1.getParticipants().equals(users))
                    .toList()
                    .get(0);
            fxmlLoader.<ChatController>getController().setIDChat(chat.getId());
            fxmlLoader.<ChatController>getController().setSuperService(this.superService);
            configurationPane.getChildren().removeAll();
            configurationPane.getChildren().setAll(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
