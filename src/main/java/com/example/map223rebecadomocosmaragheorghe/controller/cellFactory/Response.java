package com.example.map223rebecadomocosmaragheorghe.controller.cellFactory;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class Response extends AnchorPane {
    @FXML
    public Label messageLabel;
    @FXML
    public AnchorPane paneRoot;
    @FXML
    public Label nameLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public TextFlow textFlow;

    public Response() {
        FXMLLoader messageLoader = new FXMLLoader(getClass().getResource("/com/example/map223rebecadomocosmaragheorghe/theResponse-view.fxml"));
        messageLoader.setRoot(this);
        messageLoader.setController(this);
        try {
            messageLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        paneRoot.maxWidthProperty().bind(widthProperty().multiply(0.75));
        this.textFlow.setMaxWidth(580.0);
        this.textFlow.setMaxHeight(200);
    }

    public AnchorPane getPaneRoot() {
        return paneRoot;
    }

    public void setMessageLabel(String messageLabel) {
        this.messageLabel.setText(messageLabel);
        //this.messageLabel.setPadding(new Insets(5));
        this.messageLabel.setWrapText(true);
        this.messageLabel.setMaxWidth(580.0);
        this.messageLabel.setMaxHeight(200);
    }

    public void setTimeLabel(String time){this.timeLabel.setText(time);}

    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }
}

