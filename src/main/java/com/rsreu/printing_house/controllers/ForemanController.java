package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.components.DateTimePickerTableCell;
import com.rsreu.printing_house.entities.Favour;
import com.rsreu.printing_house.entities.Machine;
import com.rsreu.printing_house.entities.PrintingOrder;
import com.rsreu.printing_house.perfomance.OrderPerformance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Button;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForemanController {
    @FXML
    private Button btnCorrect;
    @FXML
    private Button btnEndOrder;
    @FXML
    private Button btnReportError;
    @FXML
    private TableColumn<OrderPerformance, LocalDateTime> deadLine;
    @FXML
    private TableColumn<OrderPerformance, LocalDateTime> endingDate;
    @FXML
    private TableColumn<OrderPerformance, String> machine;
    @FXML
    private TableColumn<OrderPerformance, String> favour;
    @FXML
    private TableView<OrderPerformance> schedule;
    private RestTemplate restTemplate;

    @FXML
    public void initialize() {
        restTemplate = new RestTemplate();
        insertDataIntoOrdersTable();
        initOrdersTable();
        Timer timer = new Timer("Updating timer", true);
        UpdateTask updateTask = new UpdateTask();
        timer.schedule(updateTask, 5000, 5000);
        App.fullScreen();
    }

    public void insertDataIntoOrdersTable() {
        PrintingOrder[] printingOrders = restTemplate.getForObject(App.getServerURL() + "/orders/get/employee/"
                + App.getEmployee().getId(), PrintingOrder[].class);
        Assert.notNull(printingOrders, "Запрос прислал null");
        favour.setCellValueFactory(new PropertyValueFactory<>("favour"));
        machine.setCellValueFactory(new PropertyValueFactory<>("machine"));
        //beginning date
        endingDate.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
        deadLine.setCellValueFactory(new PropertyValueFactory<>("deadLine"));
        //material
        //volume
        schedule.setItems(FXCollections.observableList(Stream.of(printingOrders)
                .map(OrderPerformance::toPerformance)
                .collect(Collectors.toList())));
    }

    public void initOrdersTable() {
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

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
//            TableColumn column = null;
//            TableColumn.SortType st = null;
//            if (!schedule.getSortOrder().isEmpty()) {
//                column = schedule.getSortOrder().get(0);
//                st = column.getSortType();
//            }
            if (schedule.getSortOrder().isEmpty()) {
                insertDataIntoOrdersTable();
            }
//            if (column != null) {
//                column.setSortType(st);
//            }
        }
    }
}

