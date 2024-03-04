package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.exception.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
}
