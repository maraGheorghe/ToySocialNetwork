package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.domain.Notification;
import com.example.map223rebecadomocosmaragheorghe.domain.NotificationType;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;

public class NotificationController implements PropertyChangeListener {

    private SuperService superService;
    private ObservableList<Notification> model = FXCollections.observableArrayList();
    @FXML
    ListView<Notification> notificationsListView;

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        this.superService.addPropertyChangeListener(this);
        initModel();
    }

    public void initialize() {
        setModel();
    }

    public void setModel() {
        notificationsListView.setItems(model);
        notificationsListView.setCellFactory(x -> {
            ListCellNotificationController cell = new ListCellNotificationController();
            cell.setSuperService(superService);
            return cell;
        });
    }

    private void initModel() {
        model.clear();
        model.addAll(superService.page.getEvents().
                stream().
                map(
                    event -> new Notification(NotificationType.EventNotification, event)
                    ).toList());
        model.addAll(superService.page.getRequests().
                stream().
                map(
                        request -> new Notification(NotificationType.RequestNotification, request)
                ).toList());
        model.addAll(superService.page.getMessages().
                stream().
                map(
                        message -> new Notification(NotificationType.MessageNotification, message)
                ).toList());
        Collections.shuffle(model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initModel();
    }
}
