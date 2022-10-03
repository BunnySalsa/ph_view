package com.rsreu.printing_house.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rsreu.printing_house.controllers.App;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Favour {
    private Long id;
    private String name;
    private static final RestTemplate template = new RestTemplate();

    public static List<Favour> values() {
        return List.of(Objects.requireNonNull(template.getForObject(App.getServerURL() + "/favours/get", Favour[].class)));
    }
}
