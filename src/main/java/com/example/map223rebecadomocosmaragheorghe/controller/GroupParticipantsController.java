package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.controller.cellFactory.Profile;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class GroupParticipantsController extends ListCell<UserDTO> {

        private SuperService superService;
        @FXML
        Label infoLabel;
        @FXML
        AnchorPane anchorPane;
        @FXML
        Circle profilePicture;

        public void setSuperService (SuperService superService){
            this.superService = superService;
        }

        @Override
        protected void updateItem (UserDTO user,boolean empty){
            super.updateItem(user, empty);
            if (user == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("groupParticipantsCell.fxml"));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilePicture.setStroke(Color.valueOf("#439F0375"));
            Image image = new Image(user.getPicture());
            profilePicture.setFill(new ImagePattern(image));
            infoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            infoLabel.setAlignment(Pos.CENTER);
            setText(null);
            setGraphic(anchorPane);
        }
}
