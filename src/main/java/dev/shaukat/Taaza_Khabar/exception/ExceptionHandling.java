package dev.shaukat.Taaza_Khabar.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.openqa.selenium.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandling {
    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> payloadFieldMissingExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {

        LOGGER.log(Level.INFO, "User sent an empty payload. " + "Remote address: " + request.getRemoteAddr() + " Path: " + request.getServletPath());

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("statusCode", e.getStatusCode().value());
        response.put("message", e.getAllErrors().getFirst().getDefaultMessage());
        response.put("path", request.getServletPath());


        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFoundExceptionHandler(NotFoundException e, HttpServletRequest request) {
        LOGGER.log(Level.INFO, e.getMessage() + "\nRemoteAddress " + request.getRemoteAddr() + "\nPath: " + request.getServletPath());

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("statusCode", HttpStatus.NOT_FOUND.value());
        response.put("message", e.getRawMessage());
        response.put("path", request.getServletPath());


        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralExceptionResponse> userAlreadyExistsExceptionHandler(GeneralException e) {
        return new ResponseEntity<>(new GeneralExceptionResponse(e.getMessage()), e.getStatusCode());
    }
}
