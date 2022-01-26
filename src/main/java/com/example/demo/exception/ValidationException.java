package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice //@ControllerAdvice apply globally to all controllers
@RestController
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class) //Handle validation exception instance of object
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String message = error.getDefaultMessage();
            errors.put("message",message);
        });
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class) //Handle validation exception parameter on method
    public Map<String,String> handleConstraintValidationException(ConstraintViolationException exception){
        Map<String,String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach((error) -> {
            String message = error.getMessage();
            errors.put("message",message);
        });
        return errors;
    }


}
