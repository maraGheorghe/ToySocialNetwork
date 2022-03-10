package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ListCellProfileDelete extends ListCell<UserDTO> {
    @FXML
    Label nameLabel;
    @FXML
    Label emailLabel;
    @FXML
    Button deleteButton;
    @FXML
    Button seeProfile;
    @FXML
    AnchorPane pane;
    @FXML
    Circle imageCircle;

    private SuperService superService;
    private UserDTO hoveredUser = this.getItem();

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @Override
    protected void updateItem(UserDTO user, boolean empty) {
        super.updateItem(user, empty);
        if(user == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/com/example/map223rebecadomocosmaragheorghe/profileCellWithButton-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageCircle.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(user.getPicture());
        imageCircle.setFill(new ImagePattern(image));
        nameLabel.setText(user.getFullName());
        nameLabel.setAlignment(Pos.CENTER);
        emailLabel.setText(user.getEmail());
        emailLabel.setAlignment(Pos.CENTER);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user0 = superService.userService.findOneByEmail(user.getEmail()).get();
                superService.friendshipService.deleteFriendship(superService.getUser().getId(), user0.getId());
            }
        });
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/map223rebecadomocosmaragheorghe/friendProfile-view.fxml"));
        AnchorPane root0 = null;
        try {
            root0 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene0 = new Scene(root0);
        Stage secondStage = new Stage();
        secondStage.initStyle(StageStyle.UNDECORATED);
        secondStage.setScene(scene0);
        seeProfile.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                if (this.isHover()) {
                    hoveredUser = this.getItem();
                    fxmlLoader.<FriendsProfileController>getController().setSuperService(superService);
                    fxmlLoader.<FriendsProfileController>getController().setProfile(hoveredUser);
                    secondStage.setX(superService.mainPane.getScene().getWindow().getX() + pane.getLayoutX() + 430D);
                    secondStage.setY(superService.mainPane.getScene().getWindow().getY() + pane.getLayoutY() + 200D);
                    secondStage.show();
                }
            } else {
                secondStage.hide();
            }
        });
        setText(null);
        setGraphic(pane);
    }
}
