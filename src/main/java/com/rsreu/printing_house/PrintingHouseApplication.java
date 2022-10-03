package com.rsreu.printing_house;

import com.rsreu.printing_house.controllers.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrintingHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintingHouseApplication.class, args);
        App.main(args);
    }
}
