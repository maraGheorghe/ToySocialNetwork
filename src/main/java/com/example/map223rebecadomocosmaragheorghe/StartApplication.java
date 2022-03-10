package com.example.map223rebecadomocosmaragheorghe;

import com.example.map223rebecadomocosmaragheorghe.controller.LoginController;
import com.example.map223rebecadomocosmaragheorghe.domain.validators.*;
import com.example.map223rebecadomocosmaragheorghe.repository.db.*;
import com.example.map223rebecadomocosmaragheorghe.service.*;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserRepositoryDB userRepository = new UserRepositoryDB(Constants.url, Constants.username, Constants.password);
        FriendshipRepositoryDB friendshipRepository = new FriendshipRepositoryDB(Constants.url, Constants.username, Constants.password);
        FriendRequestRepositoryDB friendRequestRepository = new FriendRequestRepositoryDB(Constants.url, Constants.username, Constants.password);
        MessageRepositoryDB messageRepository = new MessageRepositoryDB(Constants.url, Constants.username, Constants.password);
        ChatRepositoryDB chatRepositoryDB = new ChatRepositoryDB(Constants.url, Constants.username, Constants.password);
        EventRepositoryDB eventRepositoryDB = new EventRepositoryDB(Constants.url, Constants.username, Constants.password);

        UserService userService = new UserService(userRepository, new UserValidator());
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, new FriendshipValidator());
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository, friendshipRepository, new FriendRequestValidator());
        MessageService messageService = new MessageService(messageRepository, new MessageValidator());
        CommunityService communityService = new CommunityService(userRepository, friendshipRepository);
        ChatService chatService = new ChatService(chatRepositoryDB);
        EventService eventService = new EventService(eventRepositoryDB, new EventValidator());

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        fxmlLoader.<LoginController>getController().setSuperService(new SuperService(userService, friendshipService, friendRequestService, messageService, communityService, chatService, eventService, null));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Squirrel Space");
        primaryStage.getIcons().add(new Image("/images/peanuts.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        FXMLLoader fxmlLoader0 = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        AnchorPane root0 = fxmlLoader0.load();
        fxmlLoader0.<LoginController>getController().setSuperService(new SuperService(userService, friendshipService, friendRequestService, messageService, communityService, chatService, eventService, null));
        Scene scene0 = new Scene(root0);
        Stage secondStage = new Stage();
        secondStage.setTitle("Squirrel Space");
        secondStage.getIcons().add(new Image("/images/peanuts.png"));
        secondStage.setScene(scene0);
        secondStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
