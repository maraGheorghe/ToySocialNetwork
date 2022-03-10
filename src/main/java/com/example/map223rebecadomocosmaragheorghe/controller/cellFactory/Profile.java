package com.example.map223rebecadomocosmaragheorghe.controller.cellFactory;


import com.example.map223rebecadomocosmaragheorghe.controller.FriendsProfileController;
import com.example.map223rebecadomocosmaragheorghe.controller.ProfileCellController;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Profile extends AnchorPane {
    @FXML
    AnchorPane friendsPane;
    @FXML
    Rectangle profilePhoto;
    @FXML
    Label nameLabel;
    @FXML
    Button deleteButton;

    private SuperService superService;
    private UserDTO hoveredUser;

    public Profile() {
        FXMLLoader profileLoader = new FXMLLoader(getClass().getResource("/com/example/map223rebecadomocosmaragheorghe/friendsProfileCell-view.fxml"));
        profileLoader.setRoot(this);
        profileLoader.setController(this);
        try {
            profileLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        friendsPane.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                if (this.isHover()) {
                    FriendsProfileController friendsProfileController = new FriendsProfileController();
                    fxmlLoader.<FriendsProfileController>getController().setSuperService(superService);
                    fxmlLoader.<FriendsProfileController>getController().setProfile(hoveredUser);
                    secondStage.setX(superService.mainPane.getScene().getWindow().getX() + friendsPane.getLayoutX() + 90D);
                    secondStage.setY(superService.mainPane.getScene().getWindow().getY() + friendsPane.getLayoutY() + 90D);
                    secondStage.show();
                }
            } else {
                secondStage.hide();
            }
        });
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                User currentUser = superService.userService.findOneByEmail(hoveredUser.getEmail()).get();
                superService.friendshipService.deleteFriendship(superService.getUser().getId(), currentUser.getId());
            }
        });
    }

    public AnchorPane getProfilePane() {
        return friendsPane;
    }

    public void setProfilePhoto(String photoUrl){
        this.profilePhoto.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(photoUrl);
        this.profilePhoto.setFill(new ImagePattern(image));
        this.profilePhoto.setEffect(new DropShadow(+10d, 0d, +2d, Color.valueOf("#B17A50B2")));
    }

    public void setNameField(String name) {
        this.nameLabel.setText(name);
    }

    public void setService(SuperService superService) {
        this.superService = superService;
    }

    public void setCurrentUser(ProfileCellController profileCellController) {
        this.hoveredUser = profileCellController.getItem();
    }
}
