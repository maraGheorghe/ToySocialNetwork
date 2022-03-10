package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.controller.cellFactory.MessageCell;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.ChatDTO;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.scene.control.ListCell;

public class CellController extends ListCell<ChatDTO> {

    @Override
    protected void updateItem(ChatDTO chatDTO, boolean empty) {
        super.updateItem(chatDTO, empty);
        if (chatDTO != null) {
            MessageCell myCellFactory = new MessageCell();
            myCellFactory.setChatNameField(chatDTO.getName());
            myCellFactory.setChatPhoto(chatDTO.getPicture());
            if (chatDTO.getLastMessage() != null && chatDTO.getLastMessage().getDate() != null) {
                myCellFactory.setDateField(chatDTO.getLastMessage().getDate().format(Constants.DATE_TIME_FORMATTER));
                myCellFactory.setMessageField(chatDTO.getLastMessage().toString());
            }
            else {
                myCellFactory.setDateField("");
                myCellFactory.setMessageField("");
            }
            setGraphic(myCellFactory.getPaneRoot());
        }
    }
}
