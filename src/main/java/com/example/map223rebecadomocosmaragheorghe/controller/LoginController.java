package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.EncryptPassword;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    private SuperService superService;
    @FXML
    public Hyperlink signupLink;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label errorLabel;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }

    @FXML
    protected void handleLoginButton(ActionEvent actionEvent) throws IOException {
        try {
            Optional<User> optionalUser = superService.userService.findOneByEmail(usernameField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(EncryptPassword.encodePassword(passwordField.getText()))) {
                this.superService.setUser(optionalUser.get());
                this.superService.setPage();
                fxmlLoader.<AppController>getController().setSuperService(this.superService);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
            }
            else errorLabel.setText("Wrong username or password!");
        } catch (IllegalArgumentException | ValidationException exception) {
            errorLabel.setText(exception.getMessage());
        }
    }

    @FXML
    protected void handleSignUpLink(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) signupLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("signup-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        fxmlLoader.<SignUpController>getController().setSuperService(superService);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
