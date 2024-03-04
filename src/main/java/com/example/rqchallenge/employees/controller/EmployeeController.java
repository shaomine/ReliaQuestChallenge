package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class EmployeeController implements IEmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {

        try {
            log.info("getAllEmployees:: Get request received");
            List<Employee> list = employeeService.getAllEmployees();
            log.info("getAllEmployees:: Response generated successfully");
            return ResponseEntity.ok(list);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Encountered Error:" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("getEmployeeById:: Get request received for id: " + id);
        Employee employee = employeeService.getEmployeeById(id);
        log.info("getEmployeeById:: Response generated successfully for id: " + id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("getEmployeesByNameSearch:: Get request received for searchString: " + searchString);
        List<Employee> matchingEmployees = employeeService.getEmployeesByNameSearch(searchString);
        log.info("getEmployeesByNameSearch:: Response generated successfully for searchString: " + searchString);
        return ResponseEntity.ok(matchingEmployees);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("getHighestSalaryOfEmployees:: Get request received");
        Integer highestSalary = employeeService.getHighestSalary();
        log.info("getHighestSalaryOfEmployees:: Response generated successfully");
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("getTopTenHighestEarningEmployeeNames:: Get request received");
        List<String> list = employeeService.getTopTenHighestEarningEmployeeNames();
        log.info("getTopTenHighestEarningEmployeeNames:: Response generated successfully");
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        log.info("createEmployee:: Post request received");
        Employee employee = employeeService.createEmployee(employeeInput);
        log.info("createEmployee:: created successfully");
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("deleteEmployeeById:: Delete request received for id: " + id);
        String deletedEmployeeName = employeeService.deleteEmployee(id);
        log.info("deleteEmployeeById:: Deleted Successfully for id: " + id + " name: " + deletedEmployeeName);
        return ResponseEntity.ok(deletedEmployeeName);
    }
}
