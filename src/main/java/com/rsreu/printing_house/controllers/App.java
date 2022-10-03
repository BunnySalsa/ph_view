package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.entities.Employee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;


public class App extends Application {
    private static Scene scene;
    private static Stage stage;
    @Getter
    @Setter
    private static String serverURL;
    @Getter
    @Setter
    private static Employee employee;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("authorization"));
        App.stage = stage;
        App.stage.setScene(scene);
        App.stage.show();
        App.stage.setTitle("Типография");
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setSize(double x, double y) {
        stage.setHeight(x);
        stage.setWidth(y);
        stage.centerOnScreen();
    }

    public static void fullScreen() {
        stage.setMaximized(true);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
