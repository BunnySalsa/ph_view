package com.rsreu.printing_house.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeRole {

    private long employee;
    private long role;

    public EmployeeRole() {
    }
}
