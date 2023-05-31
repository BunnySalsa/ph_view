package com.rsreu.printing_house.perfomance;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@Builder
public class OrderPerformance {
    private long id;
    private String customer;
    private String employee;
    private String machine;
    private String material;
    private String favour;
    private LocalDateTime beginningDate;
    private LocalDateTime endingDate;
    private LocalDateTime deadLine;
    private long volume;
//    private PrintingOrder order;

//    public static OrderPerformance toPerformance(PrintingOrder order){
//        return OrderPerformance.builder()
//                .id(order.getId())
//                .customer(order.getCustomer().getName())
//                .employee(order.getEmployee().getName())
//                .machine(order.getMachine().getName())
//                .material(order.getMaterial().getName())
//                .favour(order.getFavour().getName())
//                .beginningDate(order.getBeginningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
//                .endingDate(order.getEndingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
//                .deadLine(order.getDeadLine().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
//                .volume(order.getVolume())
//                .order(order)
//                .build();
//    }
}
