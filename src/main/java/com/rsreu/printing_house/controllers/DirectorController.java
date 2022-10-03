package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.components.DateTimePickerTableCell;
import com.rsreu.printing_house.entities.Employee;
import com.rsreu.printing_house.entities.Favour;
import com.rsreu.printing_house.entities.Machine;
import com.rsreu.printing_house.entities.PrintingOrder;
import com.rsreu.printing_house.perfomance.OrderPerformance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectorController {
    @FXML private TableColumn<OrderPerformance, String> customer;
    @FXML private TableColumn<OrderPerformance, LocalDateTime> deadLine;
    @FXML private TableColumn<OrderPerformance, String> employee;
    @FXML private TableColumn<OrderPerformance, LocalDateTime> endingDate;
    @FXML private TableColumn<OrderPerformance, String> machine;
    @FXML private TableColumn<Machine, String> machineName;
    @FXML private TableColumn<Machine, String> machineProduction;
    @FXML private TableView<Machine> machines;
    @FXML private TableColumn<OrderPerformance, String> material;
    @FXML private TableView<OrderPerformance> orders;
    @FXML private TableColumn<OrderPerformance, String> favour;
    @FXML private TableColumn<Employee, Long> shiftColumn;
    @FXML private TableColumn<Employee, String> shiftEmployee;
    @FXML private TableView<Employee> shifts;
    private RestTemplate restTemplate;
    private static final String QUERY_RETURNED_NULL = "Запрос вернул null";


    @FXML
    public void initialize() {
        App.fullScreen();
        restTemplate = new RestTemplate();
        insertDataIntoOrders();
        insertDataIntoMachines();
        insertDataIntoShifts();
        initOrderTableComponents();
    }

    public void insertDataIntoOrders() {
        PrintingOrder[] ordersArr = restTemplate.getForObject(App.getServerURL() + "/orders/get",
                PrintingOrder[].class);
        Assert.notNull(ordersArr, QUERY_RETURNED_NULL);
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        favour.setCellValueFactory(new PropertyValueFactory<>("favour"));
        machine.setCellValueFactory(new PropertyValueFactory<>("machine"));
        endingDate.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
        deadLine.setCellValueFactory(new PropertyValueFactory<>("deadLine"));
        material.setCellValueFactory(new PropertyValueFactory<>("material"));
        employee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        //volume
        ObservableList<OrderPerformance> list = FXCollections.observableList(Stream.of(ordersArr)
                .map(OrderPerformance::toPerformance)
                .collect(Collectors.toList()));
        orders.setItems(list);
    }

    public void insertDataIntoMachines() {
        Machine[] machinesArr = restTemplate.getForObject(App.getServerURL() + "/machines/get", Machine[].class);
        Assert.notNull(machinesArr, QUERY_RETURNED_NULL);
        machineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        machineProduction.setCellValueFactory(new PropertyValueFactory<>("production"));
        ObservableList<Machine> list = FXCollections.observableList(List.of(machinesArr));
        machines.setItems(list);
    }

    public void insertDataIntoShifts() {
        Employee[] employees = restTemplate.getForObject(App.getServerURL() + "/employees/get", Employee[].class);
        Assert.notNull(employees, QUERY_RETURNED_NULL);
        shiftEmployee.setCellValueFactory(new PropertyValueFactory<>("name"));
        shiftColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ObservableList<Employee> list = FXCollections.observableList(List.of(employees));
        shifts.setItems(list);
    }

    public void initOrderTableComponents() {
        initComboBoxIntoOrders(Stream.of(Objects.requireNonNull(restTemplate
                .getForObject(App.getServerURL() + "/favours/get", Favour[].class))).map(Favour::getName)
                .collect(Collectors.toList()), favour);
        initComboBoxIntoOrders(Stream.of(Objects.requireNonNull(restTemplate
                .getForObject(App.getServerURL() + "/machines/get", Machine[].class))).map(Machine::getName)
                .collect(Collectors.toList()), machine);
        endingDate.setCellFactory(new DateTimePickerTableCell<>());
        favour.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, String> event) -> {
            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
            order.setFavour(event.getNewValue());
            order.getOrder().setFavour(Favour.values().stream().filter(x -> x.getName().equals(event.getNewValue()))
                    .findFirst().orElse(new Favour()));
            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
        });
        machine.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, String> event) -> {
            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
            order.setMachine(event.getNewValue());
            order.getOrder().setMachine(Machine.values().stream().filter(x -> x.getName().equals(event.getNewValue()))
                    .findFirst().orElse(new Machine()));
            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
        });
        endingDate.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, LocalDateTime> event) -> {
            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
            LocalDateTime newValue = event.getNewValue();
            order.getOrder().setEndingDate(Date.from(newValue.atZone(ZoneId.systemDefault()).toInstant()));
            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
        });
    }

    private void initComboBoxIntoOrders(List<String> data, TableColumn<OrderPerformance, String> column) {
        ObservableList<String> list = FXCollections.observableList(data);
        column.setCellFactory(ComboBoxTableCell.forTableColumn(list));
    }
}
