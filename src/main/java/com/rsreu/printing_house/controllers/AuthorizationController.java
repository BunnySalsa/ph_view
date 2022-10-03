package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.entities.Employee;
import com.rsreu.printing_house.entities.EmployeeRole;
import com.rsreu.printing_house.entities.PrintingOrder;
import com.rsreu.printing_house.entities.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

public class AuthorizationController {

    @FXML
    private TextField tfServerIP;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lblWarningMessage;

    public void onEnter(KeyEvent event) {
        if (event.equals(KeyEvent.VK_ENTER)) {
            authorize(new ActionEvent());
        }
    }

    public void authorize(ActionEvent event) {
        App.setServerURL("http://" + tfServerIP.getText() + ":8080");
        RestTemplate restTemplate = new RestTemplate();
        Employee employee = Employee.builder().login(tfLogin.getText()).password(pfPassword.getText()).build();
        Boolean isAuth = null;
        try {
            isAuth = Boolean.TRUE.equals(restTemplate
                    .postForObject(App.getServerURL() + "/employees/auth", employee, Boolean.class));
            if (isAuth) {
                employee = restTemplate.getForObject(App.getServerURL() + "/employees/get/login/"
                        + employee.getLogin(), Employee.class);
                Assert.notNull(employee, "Запрос сотрудника вернул null");
                employee.setPassword(pfPassword.getText());
                EmployeeRole[] employeeRoles = restTemplate.getForObject(App.getServerURL() + "/employees/role/"
                        + employee.getId(), EmployeeRole[].class);
                Assert.notNull(employeeRoles, "Запрос ролей вернул null");
                App.setEmployee(employee);
                openAppByRole(employeeRoles);
            } else {
                lblWarningMessage.setText("Неверный логин или пароль");
            }
        } catch (Exception e) {
            lblWarningMessage.setText("Сервер не запущен или неверно указан IP");
        }

    }

    public void openAppByRole(EmployeeRole[] employeeRoles) {
        RestTemplate restTemplate = new RestTemplate();
        switch (employeeRoles.length) {
            case 1: {
                try {
                    switch (Objects.requireNonNull(restTemplate.getForObject(App.getServerURL() + "/roles/get/"
                            + employeeRoles[0].getRole(), Role.class)).getName()) {
                        case "Manager": {
                            App.setRoot("manager");
                            break;
                        }
                        case "Foreman": {
                            App.setRoot("foreman");
                            break;
                        }
                        case "Director": {
                            App.setRoot("director");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 0: {
                lblWarningMessage.setText("У указанного сотрудника нет должности");
            }
        }
    }
}
