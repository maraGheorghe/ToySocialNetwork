package com.example.map223rebecadomocosmaragheorghe.controller;

import com.example.map223rebecadomocosmaragheorghe.service.DTO.MessageDTO;
import com.example.map223rebecadomocosmaragheorghe.service.DTO.UserDTO;
import com.example.map223rebecadomocosmaragheorghe.service.SuperService;
import com.example.map223rebecadomocosmaragheorghe.utils.reports.PDFWriter;
import com.example.map223rebecadomocosmaragheorghe.utils.reports.ReportsType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DatePickerController implements Initializable {
    @FXML
    public DatePicker startPicker;
    @FXML
    public DatePicker endPicker;
    @FXML
    public Label errorLabel;
    @FXML
    public Button closeButton;
    @FXML
    public TextField searchBar;
    @FXML
    public ListView<UserDTO> friendsList;
    @FXML
    public Label selectAFriend;

    private SuperService superService;
    private ReportsType type;
    private UserDTO selectedFriend;

    private ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();

    public void setSuperService(SuperService superService) {
        this.superService = superService;
        friendsList.setItems(modelFriends);
        setModelFriends();
        searchBar.textProperty().addListener(x -> {
            if (searchBar.getText().equals("")) {
                 setModelFriends();
            }
        else {
            handleSearchBar();
        }
        });
    }

    private void setModelFriends(){
        modelFriends.setAll(getAllUsersDTO());
    }

    private List<UserDTO> getAllUsersDTO() {
        List<UserDTO> usersMain = new ArrayList<>();
        superService.communityService.getAllFriendshipsOfAUser(superService.getUser().getId(), superService.communityService.getFriendsOfAUser(superService.user.getId())).
                forEach(friendshipDto -> usersMain.add(friendshipDto.getUserDto()));
        return usersMain;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendsList.setCellFactory(x -> {
            UserCell cell = new UserCell();
            cell.setSuperService(superService);
            return cell;
        });
    }

    @FXML
    public void handleSearchBar() {
        Predicate<UserDTO> p = userDto -> {
            return userDto.getFirstName().startsWith(searchBar.getText()) || userDto.getLastName().startsWith(searchBar.getText());
        };
        modelFriends.setAll(getAllUsersDTO().stream().filter(p).collect(Collectors.toList()));
    }

    public void setReportsType(ReportsType type) {
        this.type = type;
        if (type.equals(ReportsType.ACTIVITY)) {
            this.searchBar.setVisible(false);
            this.friendsList.setVisible(false);
            this.selectAFriend.setVisible(false);
        }
    }

    @FXML
    public void handleCloseButton(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        if (startPicker.getValue() == null || endPicker.getValue() == null) {
            errorLabel.setVisible(true);
            errorLabel.setText("Please choose both dates.");
            return;
        }
        if (endPicker.getValue().isAfter(LocalDate.now())) {
            errorLabel.setText("Date must be in the present or past.");
            errorLabel.setVisible(true);
            return;
        }
        if (type.equals(ReportsType.MESSAGES)) {
            this.selectedFriend = friendsList.getSelectionModel().getSelectedItem();
            if (selectedFriend == null) {
                errorLabel.setVisible(true);
                errorLabel.setText("You have to select a friend");
                return;
            }
        }

        LocalDate startDate = startPicker.getValue();
        LocalDate endDate = endPicker.getValue();
        String file = getFile();
        StringBuffer reportText = getNeededReport(startDate, endDate);

        PDFWriter pdfWriter = new PDFWriter(file);
        pdfWriter.createPdfFile();
        String heading = getHeader(startDate, endDate);
        pdfWriter.addPage(heading, reportText);
        pdfWriter.saveAndClose();

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private String getFile() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        return file.getAbsolutePath();
    }

    public String getHeader(LocalDate startDate, LocalDate endDate) {
        if (type.equals(ReportsType.ACTIVITY)) return getActivityReportHeader(startDate, endDate);
        if (type.equals(ReportsType.MESSAGES)) return getMessagesReportHeader(startDate, endDate);
        return null;

    }

    private String getActivityReportHeader(LocalDate startDate, LocalDate endDate) {
        return "Activity report for " + startDate.toString() + " - " + endDate.toString();
    }

    private String getMessagesReportHeader(LocalDate startDate, LocalDate endDate) {
        return "Messages report for " + startDate.toString() + " - " + endDate.toString();
    }

    public StringBuffer getNeededReport(LocalDate startDate, LocalDate secondDate) {
        if (type.equals(ReportsType.ACTIVITY)) return generateActivityReport(startDate, secondDate);
        if (type.equals(ReportsType.MESSAGES)) return generateMessagesReport(startDate, secondDate);
        return null;
    }

    private StringBuffer generateMessagesReport(LocalDate startDate, LocalDate endDate) {
        StringBuffer reportText = new StringBuffer();
        List<MessageDTO> reportList = superService.getMessagesWithAUserReport(selectedFriend, startDate.atStartOfDay(), endDate.atStartOfDay());
        if (reportList.isEmpty()) {
            reportText.append("You have no messages with this squirrel in this time period.");
            return reportText;
        }
        reportText.append("You chose this friend:  ").append(selectedFriend.getFullName()).append("\n\n");
        reportList.forEach(message -> {
            String text =  message.downloadConversation();
            reportText.append("\n").append(text);
                            });
        return reportText;
    }

    private StringBuffer generateActivityReport(LocalDate startDate, LocalDate endDate) {
        StringBuffer reportText = new StringBuffer();
        reportText.append("\n").append("Your messages in this time period were: ");
        List<MessageDTO> reportList = superService.getAllMessagesReport(startDate.atStartOfDay(), endDate.atStartOfDay());
        if (reportList.size() == 0) reportText.append("\n").append("-");
        else {
            reportList.forEach(message -> {
                String text = message.toReport();
                reportText.append("\n").append(text);
            });
        }
        reportText.append("\n").append("\n").append("These are the friends you made: ");
        List<UserDTO> reportList2 = superService.getAllNewFriendsReport(startDate.atStartOfDay(), endDate.atStartOfDay());
        if (reportList2.size() == 0) reportText.append("\n").append("-");
        else {
            reportList2.forEach(message -> {
                String text = message.toString();
                reportText.append("\n").append(text);
            });
        }
        return reportText;
    }
}