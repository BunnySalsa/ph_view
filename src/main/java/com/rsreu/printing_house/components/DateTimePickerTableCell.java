package com.rsreu.printing_house.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimePickerTableCell<T> implements Callback<TableColumn<T, LocalDateTime>, TableCell<T,
        LocalDateTime>> {

    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public TableCell<T, LocalDateTime> call(TableColumn<T, LocalDateTime> o) {
        return new TableCell<>() {
            private DateTimePicker datePicker;

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                if (item == getItem())
                    return;
                super.updateItem(item, empty);
                if (item == null) {
                    super.setText(null);
                } else {
                    //super.setText(item.format(dtf));
                    super.setText(item.toString());
                }
                super.setGraphic(null);
            }

            @Override
            public void startEdit() {
                if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
                    return;
                }

                if (datePicker == null) {
                    datePicker = createPicker();
                }
                datePicker.setDateTimeValue(getItem());

                super.startEdit();
                setText(null);
                setGraphic(datePicker);
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();

                setText(getItem().toString());
                setGraphic(null);
            }

            private DateTimePicker createPicker() {
                DateTimePicker picker = new DateTimePicker();

//                picker.setOnAction(event -> {
//                    this.commitEdit(picker.getDateTimeValue());
//                    event.consume();
//                });
//                picker.setOnKeyReleased(event -> {
//                    if (event.getCode() == KeyCode.ESCAPE) {
//                        this.cancelEdit();
//                        event.consume();
//                    }
//                });
                return picker;
            }
        };
    }
}
