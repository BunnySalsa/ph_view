package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.PrintingHouseApplication;
import com.rsreu.swagger.model.EmployeeDto;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;


public class App extends Application {
    private static Scene scene;
    private static Stage stage;
    @Getter
    @Setter
    private static EmployeeDto employee;
    private static FxWeaver fxWeaver;
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
            applicationContext.registerBean(Application.class, () -> App.this);
            applicationContext.registerBean(Parameters.class, this::getParameters);
            applicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.applicationContext = new SpringApplicationBuilder()
                .sources(PrintingHouseApplication.class)
                .initializers(initializer)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        App.fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AuthorizationController.class);
        scene = new Scene(root);
        App.stage = new Stage();
        App.stage.setScene(scene);
        App.stage.show();
        App.stage.setTitle("Типография");
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    public static void setRoot(Class FXController) throws IOException {
        scene.setRoot(fxWeaver.loadView(FXController.getClass()));
    }

    public static void fullScreen() {
        stage.setMaximized(true);
    }
}
