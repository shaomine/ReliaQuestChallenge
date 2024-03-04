package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee implements Serializable {
    private Integer id;
    private String employee_name;
    private Integer employee_salary;
    private Integer employee_age;
    private String profile_image;
}
