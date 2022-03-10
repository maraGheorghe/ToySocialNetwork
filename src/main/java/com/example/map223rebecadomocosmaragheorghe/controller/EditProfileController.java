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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class EditProfileController {
    @FXML
    public Circle profilePicture;
    @FXML
    public Button profilePictureButton;
    @FXML
    public TextField emailField;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Button backButton;
    @FXML
    public Button okButton;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Label errorLabel;

    private String photoUrl;
    private SuperService superService;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        Image image = new Image(superService.user.getPhotoUrl());
        profilePicture.setFill(new ImagePattern(image));
    }

    @FXML
    public void handleProfilePicture(ActionEvent actionEvent) {
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
    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("profile-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ProfileController>getController().setSuperService(this.superService);
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }

    @FXML
    public void handleOkButton(ActionEvent actionEvent) throws IOException {
        System.out.println(photoUrl);
        if (photoUrl == null) photoUrl = superService.user.getPhotoUrl();
        String email = emailField.getText();
        if (email.isEmpty()) email = superService.user.getEmail();
        String firstName = firstNameField.getText();
        if (firstName.isEmpty()) firstName = superService.user.getFirstName();
        String lastName = lastNameField.getText();
        if (lastName.isEmpty()) lastName = superService.user.getLastName();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (password.isEmpty() && confirmPassword.isEmpty()) password = superService.user.getPassword();
        else {
            password = EncryptPassword.encodePassword(password);
        }
        try {
            if (confirmPassword.isEmpty() || (!password.equals(superService.user.getPassword()))) {
                confirmPassword = EncryptPassword.encodePassword(confirmPassword);
                if (!password.equals(confirmPassword))
                    throw new ValidationException("Your passwords don't match. Please retype them!");
            }
            Optional<User> optionalUser = superService.userService.updateUser(superService.user.getId(), email, firstName, lastName, password, photoUrl);
            optionalUser.ifPresent(user -> {throw new ValidationException("Your account couldn't be updated!");});
            this.superService.setUser(superService.userService.findOneByEmail(email).get());
            goBackToProfile(okButton, this.superService, rootPane);
        }
        catch (ValidationException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }

    static void goBackToProfile(Button okButton, SuperService superService, AnchorPane rootPane) throws IOException {
        Stage stage = (Stage) okButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("profile-view.fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        fxmlLoader.<ProfileController>getController().setSuperService(superService);
        fxmlLoader.<ProfileController>getController().setProfile();
        rootPane.getChildren().removeAll();
        rootPane.getChildren().setAll(parent);
    }
}
