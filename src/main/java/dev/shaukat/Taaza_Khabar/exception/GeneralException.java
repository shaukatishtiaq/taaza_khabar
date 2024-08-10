package dev.shaukat.Taaza_Khabar.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {
    private final String message;
    private final HttpStatus statusCode;


    public GeneralException(String message, HttpStatus statusCode) {
        super(message);

        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
