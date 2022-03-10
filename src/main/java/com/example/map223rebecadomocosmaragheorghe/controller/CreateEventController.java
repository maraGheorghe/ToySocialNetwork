package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.ValidationException;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class CreateEventController {
    private SuperService superService;
    @FXML
    public Button createEventButton;
    @FXML
    public Button uploadPictureButton;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public DatePicker datePicker;
    @FXML
    Spinner<LocalTime> timeSpinner;
    @FXML
    public Label invalidLabel;
    private String photoUrl = Constants.photoUrlEvent;


    SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
        {
            setValue(defaultValue());
        }

        private LocalTime defaultValue() {
            return LocalTime.now().truncatedTo(ChronoUnit.HOURS);
        }

        @Override
        public void decrement(int steps) {
            LocalTime value = getValue();
            setValue(value == null ? defaultValue() : value.minusMinutes(15));
        }

        @Override
        public void increment(int steps) {
            LocalTime value = getValue();
            setValue(value == null ? defaultValue() : value.plusMinutes(15));
        }
    };

    @FXML
    public void setSuperService(SuperService superService) {
        this.superService = superService;
        timeSpinner.setValueFactory(factory);
    }


    public void handleCreateEventButton() {
        try {
            superService.eventService.addEvent(nameTextField.getText(), descriptionTextArea.getText(), datePicker.getValue().atTime(timeSpinner.getValue()), photoUrl, superService.getUser());
            List <Event> events = superService.eventService.getEventsMadeByAnUser(superService.user.getId());
            int last = events.size() - 1;
            System.out.println(last);
            Event event = events.get(last);
            superService.eventService.addParticipant(superService.user.getId(), event.getId());
            Stage stage = (Stage) createEventButton.getScene().getWindow();
            stage.close();
        }
        catch (ValidationException e) {
            invalidLabel.setText(e.getMessage());
        }
    }

    @FXML
    protected void handleUploadPictureButton(ActionEvent actionEvent) throws IOException {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null)
            photoUrl = file.toURI().toString();
    }
}
