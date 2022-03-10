package com.example.map223rebecadomocosmaragheorghe.controller.cellFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class MyMessage extends AnchorPane {
    @FXML
    public Label messageLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public AnchorPane paneRoot;
    @FXML
    public TextFlow textFlow;
    @FXML
    public AnchorPane mainPane;

    public MyMessage() {
        FXMLLoader messageLoader = new FXMLLoader(getClass().getResource("/com/example/map223rebecadomocosmaragheorghe/myMessage-view.fxml"));
        messageLoader.setRoot(this);
        messageLoader.setController(this);
        try{
            messageLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        paneRoot.maxWidthProperty().bind(widthProperty().multiply(0.75));
        textFlow.setMaxWidth(580.0);
        this.textFlow.setMaxHeight(200);
    }

    public AnchorPane getPaneRoot() {
        return mainPane;
    }

    public void setMessageLabel(String messageLabel) {
        this.messageLabel.setText(messageLabel);
        this.messageLabel.setWrapText(true);
        this.messageLabel.setMaxWidth(580.0);
        this.messageLabel.setMaxHeight(200);
    }

    public void setTimeLabel(String time){this.timeLabel.setText(time);}
}
