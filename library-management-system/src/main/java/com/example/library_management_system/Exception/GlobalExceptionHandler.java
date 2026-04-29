package com.example.library_management_system.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",resourceNotFoundException.getMessage()));
    }


    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Map<String,String>> handleBookNotAvailableException(BookNotAvailableException bookNotAvailableException){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",bookNotAvailableException.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String,String> errors=new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error->{
            String field=((FieldError) error).getField();
            String message=error.getDefaultMessage();
            errors.put(field,message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
