package com.example.rqchallenge.employees.exception;

public class BadRequestArguments extends RuntimeException {
    public BadRequestArguments(String msg) {
        super(msg);
    }
}
