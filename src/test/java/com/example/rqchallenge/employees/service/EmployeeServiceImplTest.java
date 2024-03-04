package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private static final String resourceBaseurl = "https://dummy.restapiexample.com";

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(restTemplate);
    }

    @Test
    void getEmployeeById() {
        String id = "1";
        String url = resourceBaseurl + "/api/v1/employee/" + id;
        String jsonString = "{\n" +
                "    \"status\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"id\": 1,\n" +
                "        \"employee_name\": \"Tiger Nixon\",\n" +
                "        \"employee_salary\": 320800,\n" +
                "        \"employee_age\": 61,\n" +
                "        \"profile_image\": \"\"\n" +
                "    },\n" +
                "    \"message\": \"Successfully! Record has been fetched.\"\n" +
                "}";

        Mockito.when(restTemplate.getForEntity(url, String.class))
                .thenReturn(new ResponseEntity<>(jsonString, HttpStatus.OK));

        Employee employee = employeeService.getEmployeeById(id);
        Assertions.assertEquals(Integer.parseInt(id), employee.getId());
    }


    @Test
    void getAllEmployees() {
        String url = resourceBaseurl + "/api/v1/employees";
        String jsonString = "{\n" +
                "    \"status\": \"success\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": 1,\n" +
                "            \"employee_name\": \"Tiger Nixon\",\n" +
                "            \"employee_salary\": 320800,\n" +
                "            \"employee_age\": 61,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 2,\n" +
                "            \"employee_name\": \"Garrett Winters\",\n" +
                "            \"employee_salary\": 170750,\n" +
                "            \"employee_age\": 63,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 3,\n" +
                "            \"employee_name\": \"Ashton Cox\",\n" +
                "            \"employee_salary\": 86000,\n" +
                "            \"employee_age\": 66,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 4,\n" +
                "            \"employee_name\": \"Cedric Kelly\",\n" +
                "            \"employee_salary\": 433060,\n" +
                "            \"employee_age\": 22,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 5,\n" +
                "            \"employee_name\": \"Airi Satou\",\n" +
                "            \"employee_salary\": 162700,\n" +
                "            \"employee_age\": 33,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 21,\n" +
                "            \"employee_name\": \"Jenette Caldwell\",\n" +
                "            \"employee_salary\": 345000,\n" +
                "            \"employee_age\": 30,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 22,\n" +
                "            \"employee_name\": \"Yuri Berry\",\n" +
                "            \"employee_salary\": 675000,\n" +
                "            \"employee_age\": 40,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 23,\n" +
                "            \"employee_name\": \"Caesar Vance\",\n" +
                "            \"employee_salary\": 106450,\n" +
                "            \"employee_age\": 21,\n" +
                "            \"profile_image\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 24,\n" +
                "            \"employee_name\": \"Doris Wilder\",\n" +
                "            \"employee_salary\": 85600,\n" +
                "            \"employee_age\": 23,\n" +
                "            \"profile_image\": \"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"message\": \"Successfully! All records has been fetched.\"\n" +
                "}";

        Mockito.when(restTemplate.getForEntity(url, String.class))
                .thenReturn(new ResponseEntity<>(jsonString, HttpStatus.OK));
        List<Employee> employees;
        try {
            employees = employeeService.getAllEmployees();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(9, employees.size());
    }

    @Test
    void createEmployee() {
        String url = resourceBaseurl + "/api/v1/create";
        String jsonString = "{\n" +
                "    \"status\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"name\": \"test\",\n" +
                "        \"salary\": \"123\",\n" +
                "        \"age\": \"23\",\n" +
                "        \"id\": 25\n" +
                "    }\n" +
                "}";
        Map<String, Object> input = new HashMap<>();
        input.put("name", "Test");
        input.put("age", "23");
        input.put("salary", "123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String reqBody = null;
        try {
            reqBody = JacksonMapper.getObjectMapper().writeValueAsString(input);
        } catch (JsonProcessingException je) {
        }
        HttpEntity<String> request = new HttpEntity<>(reqBody, headers);
        Mockito.when(restTemplate.postForEntity(url, request, String.class))
                .thenReturn(new ResponseEntity<>(jsonString, HttpStatus.OK));
        Employee employee = employeeService.createEmployee(input);
        Assertions.assertEquals("test", employee.getEmployee_name());
        Assertions.assertEquals(23, employee.getEmployee_age());
        Assertions.assertEquals(123, employee.getEmployee_salary());
    }
}