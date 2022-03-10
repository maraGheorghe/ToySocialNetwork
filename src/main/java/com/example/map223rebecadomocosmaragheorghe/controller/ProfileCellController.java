package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.controller.cellFactory.Profile;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import javafx.scene.control.ListCell;

public class ProfileCellController extends ListCell<UserDTO> {
    private SuperService superService;

    @Override
    protected void updateItem(UserDTO userDTO, boolean empty) {
        super.updateItem(userDTO, empty);
        if (userDTO != null) {
            Profile profile = new Profile();
            profile.setService(superService);
            profile.setCurrentUser(this);
            profile.setProfilePhoto(userDTO.getPicture());
            profile.setNameField(userDTO.getFullName());
            setGraphic(profile.getProfilePane());
        }
    }


    public void setService(SuperService superService) {
        this.superService = superService;
    }

}
