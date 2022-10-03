package com.rsreu.printing_house.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rsreu.printing_house.controllers.App;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintingOrder {
    private Long id;
    private Customer customer;
    private Machine machine;
    private Employee employee;
    private Material material;
    private Favour favour;
    private Date beginningDate;
    private Date endingDate;
    private Date deadLine;
    private Long volume;
    private static final RestTemplate template = new RestTemplate();

    public static List<PrintingOrder> values() {
        return List.of(Objects.requireNonNull(template.getForObject(App.getServerURL() + "/orders/get", PrintingOrder[].class)));
    }
}
