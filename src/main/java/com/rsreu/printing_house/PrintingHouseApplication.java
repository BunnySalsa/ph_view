package com.rsreu.printing_house;

import com.rsreu.printing_house.controllers.App;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.rsreu.swagger.api")
public class PrintingHouseApplication {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
