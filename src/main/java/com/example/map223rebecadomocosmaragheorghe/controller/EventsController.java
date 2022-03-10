package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.StartApplication;
import com.example.map223rebecadomocosmaragheorghe.domain.Event;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.events.EventChangeEvent;
import com.example.map223rebecadomocosmaragheorghe.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EventsController implements Observer<EventChangeEvent> {
    private SuperService superService;
    private ObservableList<Event> modelEvents = FXCollections.observableArrayList();
    @FXML
    ListView<Event> listView;
    @FXML
    Button addEventButton;
    @FXML
    Button previousButton;
    @FXML
    Button nextButton;
    @FXML
    Pane rootPane;
    @FXML
    Label pageLabel;
    int currentPage = 0;
    int maxPage;
    int recordsOnPage = 3;


    public void setSuperService(SuperService superService) {
        this.superService = superService;
        initModelEvents();
        this.superService.eventService.addObserver(this);
    }

    public void initialize() {
        listView.setItems(modelEvents);
        listView.setCellFactory(x -> {
            ListCellEventController cell = new ListCellEventController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    private void initModelEvents() {
        Iterable<Event> events = superService.eventService.findAllEvents();
        List<Event> eventsList = new ArrayList<>();
        events.forEach(eventsList::add);
        modelEvents.setAll(eventsList);
        int noOfEvents = superService.eventService.getNoOfEvents();
        maxPage = noOfEvents / recordsOnPage + (noOfEvents % recordsOnPage != 0 ? 0 : -1);
        loadModel();
    }

    public void loadModel() {
        Iterable<Event> events = superService.eventService.getSomeEvents(currentPage * recordsOnPage, recordsOnPage);
        List<Event> eventsList = new ArrayList<>();
        events.forEach(eventsList::add);
        modelEvents.setAll(eventsList);
        pageLabel.setText((currentPage + 1) + "/" + (maxPage + 1));
        if(currentPage == 0) {
            previousButton.setVisible(false);
            nextButton.setVisible(true);
        }
        if(currentPage == maxPage) {
            previousButton.setVisible(true);
            nextButton.setVisible(false);
        }
    }

    public void handleAddEventButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("createAnEvent-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        fxmlLoader.<CreateEventController>getController().setSuperService(this.superService);
        Scene scene = new Scene(root);
        Stage secondStage = new Stage();
        secondStage.setTitle("Create an Event");
        secondStage.setScene(scene);
        secondStage.show();
    }

    @FXML
    public void handleShowDetails(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("event-details-view.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Long IDEvent;
            Optional<Event> event = Optional.ofNullable(listView.getSelectionModel().getSelectedItem());
            if (event.isPresent()) {
                IDEvent = listView.getSelectionModel().getSelectedItem().getId();
                fxmlLoader.<ShowEventController>getController().setIDEvent(IDEvent);
                fxmlLoader.<ShowEventController>getController().setSuperService(this.superService);
            }
            rootPane.getChildren().removeAll();
            rootPane.getChildren().setAll(parent);
        }
    }

    public void handleNextButton() {
        previousButton.setVisible(true);
        if(currentPage != maxPage) {
            currentPage++;
            loadModel();
        }
    }

    public void handlePreviousButton() {
        nextButton.setVisible(true);
        if(currentPage != 0) {
            currentPage--;
            loadModel();
        }
    }

    @Override
    public void update(EventChangeEvent eventChangeEvent) {
        initModelEvents();
    }
}