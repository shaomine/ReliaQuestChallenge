package com.example.rqchallenge.employees.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    public static final String DUMMY_TESTING_SERVER_ERROR = "DummyTesting Server Error";
    public static final String CLIENT_ERROR = "Client Server Error";

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError() ||
                httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            log.error(DUMMY_TESTING_SERVER_ERROR + " " + httpResponse.getStatusCode());
            throw new HttpServerErrorException(httpResponse.getStatusCode(), DUMMY_TESTING_SERVER_ERROR);
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            log.error(CLIENT_ERROR + " " + httpResponse.getStatusCode());
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        }
    }
}
