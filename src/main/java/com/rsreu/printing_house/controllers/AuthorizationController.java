package com.rsreu.printing_house.controllers;

import com.rsreu.swagger.api.EmployeeControllerApiClient;
import com.rsreu.swagger.api.RoleControllerApiClient;
import com.rsreu.swagger.model.AuthDto;
import com.rsreu.swagger.model.EmployeeDto;
import com.rsreu.swagger.model.RoleDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FxmlView("/fxml/authorization.fxml")
public class AuthorizationController {

    private final EmployeeControllerApiClient employeeClient;
    private final RoleControllerApiClient roleClient;

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
        lblWarningMessage.setVisible(false);
        AuthDto authDto = new AuthDto();
        authDto.setUsername(tfLogin.getText());
        authDto.setPassword(pfPassword.getText());
        EmployeeDto employee = employeeClient.authorize(authDto).getBody();
        if (employee != null) {
            RoleDto roleDto = roleClient.getRoleById(employee.getRole()).getBody();
            App.setEmployee(employee);
            if (roleDto != null) {
                openAppByRole(roleDto.getName());
            } else {
                lblWarningMessage.setText("Ошибка: нет доступа для роли сотрудника");
                lblWarningMessage.setVisible(true);
            }
        } else {
            lblWarningMessage.setText("Неверный логин или пароль");
        }
    }

    public void openAppByRole(String role) {
        try {
            switch (role) {
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
                default: {
                    lblWarningMessage.setText("Ошибка: нет доступа для роли сотрудника");
                    lblWarningMessage.setVisible(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
