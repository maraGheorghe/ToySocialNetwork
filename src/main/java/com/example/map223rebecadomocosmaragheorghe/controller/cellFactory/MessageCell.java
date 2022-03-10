package com.example.map223rebecadomocosmaragheorghe.controller.cellFactory;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MessageCell extends AnchorPane{
    @FXML
    public Label chatNameField;
    @FXML
    public Circle chatPhoto;
    @FXML
    public Label messageField;
    @FXML
    public Label dateField;
    @FXML
    public AnchorPane paneRoot;

    public MessageCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/map223rebecadomocosmaragheorghe/cell-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getPaneRoot() {
        return paneRoot;
    }

    public void setChatNameField(String chatNameField) {
        this.chatNameField.setText(chatNameField);
    }

    public void setChatPhoto(String chatPhoto) {
        this.chatPhoto.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(chatPhoto);
        this.chatPhoto.setFill(new ImagePattern(image));
        this.chatPhoto.setEffect(new DropShadow(+10d, 0d, +2d, Color.valueOf("#B17A50B2")));
    }

    public void setMessageField(String messageField) {
        this.messageField.setText(messageField);
    }

    public void setDateField(String dateField) {
        this.dateField.setText(dateField);
    }
}
