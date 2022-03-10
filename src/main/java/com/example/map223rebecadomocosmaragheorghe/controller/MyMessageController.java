package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.controller.cellFactory.MyMessage;
import com.example.map223rebecadomocosmaragheorghe.controller.cellFactory.Response;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.MessageDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.scene.control.ListCell;

import java.text.Format;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MyMessageController extends ListCell<MessageDTO> {

    private SuperService superService;

    @Override
    protected void updateItem(MessageDTO messageDTO, boolean empty) {
        super.updateItem(messageDTO, empty);
        if (messageDTO != null) {
            if (messageDTO.getFrom().equals(superService.getUser())) {
                MyMessage myMessage = new MyMessage();
                myMessage.setMessageLabel(messageDTO.getBody());
                myMessage.setTimeLabel(String.valueOf(messageDTO.getDate().getHour())+":"+String.valueOf(messageDTO.getDate().getMinute()));
                setGraphic(myMessage.getPaneRoot());
                this.getStylesheets().add(getClass().getResource("/css/listViewDisplay.css").toString());
            }
            else {
                Response response = new Response();
                response.setMessageLabel(messageDTO.getBody());
                response.setNameLabel(messageDTO.getFrom().getCompleteName());
                response.setTimeLabel(String.valueOf(messageDTO.getDate().getHour())+":"+String.valueOf(messageDTO.getDate().getMinute()));
                setGraphic(response.getPaneRoot());
            }
        }
    }

    public void setSuperService(SuperService superService) {
        this.superService = superService;
    }
}
