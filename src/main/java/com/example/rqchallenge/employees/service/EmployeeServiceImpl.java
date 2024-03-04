package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.exception.BadRequestArguments;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    public static final String NAME_TAG = "name";
    public static final String AGE_TAG = "age";
    public static final String SALARY_TAG = "salary";
    public static final String ID = "id";
    private final RestTemplate restTemplate;
    private final String resourceBaseurl = "https://dummy.restapiexample.com";

    public EmployeeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> getAllEmployees() throws IOException {
        String url = resourceBaseurl + "/api/v1/employees";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode eNode = root.path("data");
        if (eNode.isNull()) {
            log.error("No employee info present");
            throw new EmployeeNotFoundException("No employee info present");
        }
        List<Employee> employees = objectMapper.readValue(objectMapper.writeValueAsString(eNode),
                new TypeReference<>() {
                });
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) {

        String url = resourceBaseurl + "/api/v1/employee/" + id;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JsonNode root = null;
        try {
            root = JacksonMapper.getObjectMapper().readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        JsonNode eNode = root.path("data");
        if (eNode.isNull()) {
            log.error("No employee info present for id:" + id);
            throw new EmployeeNotFoundException("No employee info present for id:" + id);
        }
        Employee employee = JacksonMapper.getObjectMapper().convertValue(eNode, Employee.class);
        return employee;
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchKey) {
        List<Employee> employees;
        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Employee> matchingEmployees = employees.stream()
                .filter(e -> e.getEmployee_name().toLowerCase().contains(searchKey.toLowerCase()))
                .collect(Collectors.toList());
        return matchingEmployees;
    }

    @Override
    public Integer getHighestSalary() {
        List<Employee> employees = null;
        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        OptionalInt max = employees
                .stream()
                .mapToInt(e -> e.getEmployee_salary())
                .max();
        if (max.isPresent()) return max.getAsInt();
        throw new RuntimeException("No Such Element");
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = null;
        try {
            employees = getAllEmployees();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        List<String> res = new ArrayList<>();
        Collections.sort(employees, (o1, o2) -> o2.getEmployee_salary() - o1.getEmployee_salary());
        int count = 0;
        for (Employee e : employees) {
            res.add(e.getEmployee_name());
            count++;
            if (count == 10) break;
        }
        return res;
    }

    @Override
    public Employee createEmployee(Map<String, Object> input) {
        try {
            String url = resourceBaseurl + "/api/v1/create";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = parseRequestBody(input);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> createdEmployee = restTemplate.postForEntity(url, request, String.class);
            JsonNode root = JacksonMapper.getObjectMapper().readTree(createdEmployee.getBody());
            if (root.has("status") && !root.get("status").asText().equals("success")) {
                log.error("Create api returned status as false");
                throw new RuntimeException("Create api status is false");
            }
            JsonNode eNode = root.path("data");
            Employee employee = constructEmployee(eNode);
            return employee;
        } catch (JsonProcessingException je) {
            log.error("Error Encountered while creating employee, error:", je);
            throw new RuntimeException(je.getMessage());
        }
    }

    @Override
    public String deleteEmployee(String id) {

        Employee employee = null;
        try {
            employee = getEmployeeById(id);
        } catch (EmployeeNotFoundException e) {
            log.error("deleteEmployee:: Employee Not Present with id:" + id);
            throw new EmployeeNotFoundException(e.getMessage());
        }
        String url = resourceBaseurl + "/api/v1/delete/" + id;
        restTemplate.delete(url);
        return employee.getEmployee_name();
    }

    private String parseRequestBody(Map<String, Object> input) {
        checkSanitization(input);
        try {
            String reqBody = JacksonMapper.getObjectMapper().writeValueAsString(input);
            return reqBody;
        } catch (JsonProcessingException je) {
            throw new RuntimeException(je.getMessage());
        }
    }

    private Employee constructEmployee(JsonNode employeeNode) {
        Employee employee = new Employee();
        if (employeeNode.has(ID)) {
            employee.setId(employeeNode.get(ID).asInt());
        }

        if (employeeNode.has(AGE_TAG)) {
            employee.setEmployee_age(employeeNode.get(AGE_TAG).asInt());
        }

        if (employeeNode.has(NAME_TAG)) {
            employee.setEmployee_name(employeeNode.get(NAME_TAG).asText());
        }

        if (employeeNode.has(SALARY_TAG)) {
            employee.setEmployee_salary(employeeNode.get(SALARY_TAG).asInt());
        }
        return employee;
    }

    private void checkSanitization(Map<String, Object> input) {
        if (!input.containsKey(NAME_TAG) && !input.containsKey(AGE_TAG) && !input.containsKey(SALARY_TAG)) {
            String errorMsg = "Response body provided is in incorrect format, provide atleast one tag";
            log.error(errorMsg);
            throw new BadRequestArguments(errorMsg);
        }

        if (input.containsKey(AGE_TAG)) {
            try {
                Integer.parseInt(String.valueOf(input.get(AGE_TAG)));
            } catch (NumberFormatException nf) {
                String errorMsg = "age datatype should be integer";
                log.error(errorMsg);
                throw new BadRequestArguments(errorMsg);
            }
        }

        if (input.containsKey(SALARY_TAG)) {
            try {
                Integer.parseInt(String.valueOf(input.get(SALARY_TAG)));
            } catch (NumberFormatException nf) {
                String errorMsg = "salary datatype should be integer";
                log.error(errorMsg);
                throw new BadRequestArguments(errorMsg);
            }
        }
    }
}
