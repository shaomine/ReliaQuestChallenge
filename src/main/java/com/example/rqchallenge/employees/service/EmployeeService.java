package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<Employee> getAllEmployees() throws IOException;

    Employee getEmployeeById(String id);

    List<Employee> getEmployeesByNameSearch(String searchString);

    Integer getHighestSalary();

    List<String> getTopTenHighestEarningEmployeeNames();

    Employee createEmployee(Map<String, Object> input);

    String deleteEmployee(String id);
}
