package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.User;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.EncryptPassword;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SignUpController {
    private SuperService superService;
    @FXML
    public Button signUpButton;
    @FXML
    public Button backButton;
    @FXML
    public Button profilePictureButton;
    @FXML
    public TextField emailField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public PasswordField passwordFieldSignUp;
    @FXML
    public PasswordField confirmPasswordFieldSignUp;
    @FXML
    public Label invalidLabel;
    @FXML
    public Circle profilePicture;

    private String photoUrl = Constants.photoUrlProfile;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        profilePicture.setStroke(Color.valueOf("#439F0375"));
        Image image = new Image(photoUrl);
        profilePicture.setFill(new ImagePattern(image));
        profilePicture.setEffect(new DropShadow(+10d, 0d, +2d, Color.valueOf("#B17A50B2")));
    }

    @FXML
    protected void handleSignUpButton(ActionEvent actionEvent) throws IOException {
        String email = emailField.getText();
        String password = passwordFieldSignUp.getText();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        try {
            if(!password.equals(confirmPasswordFieldSignUp.getText()))
                throw new ValidationException("Wrong password!");
            Optional<User> optionalUser = superService.userService.findOneByEmail(email);
            optionalUser.ifPresent(user -> {throw new ValidationException("You already have an account!");});
            superService.userService.addUser(email, firstNameField.getText(), lastNameField.getText(), EncryptPassword.encodePassword(password), photoUrl);
            this.superService.setUser(superService.userService.findOneByEmail(email).get());
            this.superService.setPage();
            fxmlLoader.<AppController>getController().setSuperService(superService);
            stage.setScene(scene);
        }
        catch (ValidationException e) {
            invalidLabel.setText(e.getMessage());
        }
    }

    @FXML
    protected void handleProfilePicture(ActionEvent actionEvent) throws IOException {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            photoUrl = file.toURI().toString();
            Image image = new Image(photoUrl);
            profilePicture.setFill(new ImagePattern(image));
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        fxmlLoader.<LoginController>getController().setSuperService(superService);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}