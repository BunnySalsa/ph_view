package com.rsreu.printing_house.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rsreu.printing_house.controllers.App;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Machine {
    private Long id;
    private String name;
    private int production;
    private static final RestTemplate template = new RestTemplate();

    public Machine() {
    }

    public static List<Machine> values() {
        return List.of(Objects.requireNonNull(template.getForObject(App.getServerURL() + "/machines/get",
                Machine[].class)));
    }
}
