package com.example.rqchallenge.employees.exception;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
