package com.rsreu.printing_house.controllers;

import com.rsreu.printing_house.components.DateTimePickerTableCell;
import com.rsreu.printing_house.components.GanttChart;
import com.rsreu.printing_house.perfomance.OrderPerformance;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManagerController {
    @FXML private TableColumn<OrderPerformance, LocalDateTime> deadLine;
    @FXML private TableColumn<OrderPerformance, String> employee;
    @FXML private TableColumn<OrderPerformance, LocalDateTime> endingDate;
//    @FXML private TableColumn<Machine, String> machineName;
//    @FXML private TableColumn<OrderPerformance, String> machine;
//    @FXML private TableColumn<Machine, Long> machineProduction;
//    @FXML private TableView<Machine> machines;
    @FXML private TableColumn<OrderPerformance, String> material;
    @FXML private TableView<OrderPerformance> orders;
    @FXML private TableColumn<OrderPerformance, String> favour;
    @FXML private AnchorPane chartPane;
    private RestTemplate restTemplate;

    @FXML
    public void initialize() {
        App.fullScreen();
        restTemplate = new RestTemplate();
        insertDataIntoOrdersTable();
        insertDataIntoMachineTable();
        initOrderTableComponents();
        Platform.runLater(this::printGanttDiagram);
    }

    public void insertDataIntoOrdersTable() {
        try {
//            PrintingOrder[] ordersArr = restTemplate.getForObject(App.getServerURL() + "/orders/get",
//                    PrintingOrder[].class);
//            Assert.notNull(ordersArr, "Запрос вернул null");
//            favour.setCellValueFactory(new PropertyValueFactory<>("favour"));
//            machine.setCellValueFactory(new PropertyValueFactory<>("machine"));
//            endingDate.setCellValueFactory(new PropertyValueFactory<>("endingDate"));
//            deadLine.setCellValueFactory(new PropertyValueFactory<>("deadLine"));
//            material.setCellValueFactory(new PropertyValueFactory<>("material"));
//            employee.setCellValueFactory(new PropertyValueFactory<>("employee"));
//            //volume
//            ObservableList<OrderPerformance> list = FXCollections.observableList(Stream.of(ordersArr)
//                    .map(OrderPerformance::toPerformance)
//                    .collect(Collectors.toList()));
//            orders.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDataIntoMachineTable() {
        try {
//            Machine[] machinesArr = restTemplate.getForObject(App.getServerURL() + "/machines/get", Machine[].class);
//            Assert.notNull(machinesArr, "Запрос вернул null");
//            machineName.setCellValueFactory(new PropertyValueFactory<>("name"));
//            machineProduction.setCellValueFactory(new PropertyValueFactory<>("production"));
//            ObservableList<Machine> list = FXCollections.observableList(List.of(machinesArr));
//            machines.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initOrderTableComponents() {
//        initComboBoxIntoOrders(Stream.of(Objects.requireNonNull(restTemplate
//                .getForObject(App.getServerURL() + "/favours/get", Favour[].class))).map(Favour::getName)
//                .collect(Collectors.toList()), favour);
//        initComboBoxIntoOrders(Stream.of(Objects.requireNonNull(restTemplate
//                .getForObject(App.getServerURL() + "/machines/get", Machine[].class))).map(Machine::getName)
//                .collect(Collectors.toList()), machine);
//        endingDate.setCellFactory(new DateTimePickerTableCell<>());
//        favour.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, String> event) -> {
//            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
//            order.setFavour(event.getNewValue());
//            order.getOrder().setFavour(Favour.values().stream().filter(x -> x.getName().equals(event.getNewValue()))
//                    .findFirst().orElse(new Favour()));
//            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
//        });
//        machine.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, String> event) -> {
//            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
//            order.setMachine(event.getNewValue());
//            order.getOrder().setMachine(Machine.values().stream().filter(x -> x.getName().equals(event.getNewValue()))
//                    .findFirst().orElse(new Machine()));
//            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
//        });
//        endingDate.setOnEditCommit((TableColumn.CellEditEvent<OrderPerformance, LocalDateTime> event) -> {
//            OrderPerformance order = event.getTableView().getItems().get(event.getTablePosition().getRow());
//            LocalDateTime newValue = event.getNewValue();
//            order.getOrder().setEndingDate(Date.from(newValue.atZone(ZoneId.systemDefault()).toInstant()));
//            restTemplate.put(App.getServerURL() + "/orders/update/" + order.getId(), order.getOrder());
//        });
    }


    public void printGanttDiagram() {
        CategoryAxis yAxis = new CategoryAxis();
        NumberAxis xAxis = new NumberAxis();
        xAxis.forceZeroInRangeProperty().set(false);
        xAxis.setLowerBound(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).getTime());
        xAxis.tickLabelFormatterProperty().set(new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return new Date(number.longValue()).toString();
            }

            @Override
            public Number fromString(String s) {
                return Date.parse(s);
            }
        });
        XYChart<Number, String> ganttDia = new GanttChart<>(xAxis, yAxis);
        chartPane.getChildren().add(ganttDia);
//        PrintingOrder[] list = Objects.requireNonNull(restTemplate.getForObject(App.getServerURL() +
//                        "/orders/get",
//                PrintingOrder[].class));
//        List<Machine> machines = Arrays.stream(list).map(PrintingOrder::getMachine).distinct().collect(Collectors.toList());
//        for (Machine machine : machines) {
//            XYChart.Series series = new XYChart.Series();
//            List<OrderPerformance> orders =
//                    Arrays.stream(list).filter(x -> x.getMachine().equals(machine)).map(OrderPerformance::toPerformance)
//                    .collect(Collectors.toList());
//            for (OrderPerformance order : orders) {
//                series.getData().add(new XYChart.Data<>(Date.from(order.getBeginningDate().atZone(ZoneId.systemDefault()).toInstant()).getTime(),
//                        machine.getName(), new GanttChart.ExtraData(1, "-fx-background-color:rgba(0,128,0,0.7)")));
//            }
//            ganttDia.getData().add(series);
//        }
    }

    private void initComboBoxIntoOrders(List<String> data, TableColumn<OrderPerformance, String> column) {
        ObservableList<String> list = FXCollections.observableList(data);
        column.setCellFactory(ComboBoxTableCell.forTableColumn(list));
    }
}
