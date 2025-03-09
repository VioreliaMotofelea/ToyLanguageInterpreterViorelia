package com.example.a7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        HelloController mainWindowController = loadMainWindow(primaryStage);
        loadSelectWindow(mainWindowController);
    }

    private HelloController loadMainWindow(Stage primaryStage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent mainWindow = mainLoader.load();

        HelloController mainWindowController = mainLoader.getController();

        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(mainWindow, 1000, 800));
        primaryStage.show();

        return mainWindowController;
    }

    private void loadSelectWindow(HelloController mainWindowController) throws IOException {
        FXMLLoader secondaryLoader = new FXMLLoader(getClass().getResource("secondary-view.fxml"));
        Parent selectWindow = secondaryLoader.load();

        SecondaryController selectWindowController = secondaryLoader.getController();
        selectWindowController.set_main_window_controller(mainWindowController);

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Select Window");
        secondaryStage.setScene(new Scene(selectWindow, 600, 650));
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}