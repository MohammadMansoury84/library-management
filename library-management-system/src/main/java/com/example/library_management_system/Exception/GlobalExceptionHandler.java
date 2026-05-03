package com.example.library_management_system.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String,Object> buildErrors(HttpStatus status, String massage, WebRequest webRequest){
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",status.value());
        body.put("error",status.getReasonPhrase());
        body.put("massage",massage);
        body.put("path",webRequest.getDescription(false).replace("uri",""));
        return body;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,WebRequest webRequest){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrors(HttpStatus.NOT_FOUND, resourceNotFoundException.getMessage(), webRequest));
    }


    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Map<String,Object>> handleBookNotAvailableException(BookNotAvailableException bookNotAvailableException,WebRequest webRequest){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrors(HttpStatus.NOT_FOUND, bookNotAvailableException.getMessage(), webRequest));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest webRequest){
        Map<String,String> errors=new LinkedHashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error->{
            String field=((FieldError) error).getField();
            String message=error.getDefaultMessage();
            errors.put(field,message);
        });

        Map<String,Object> body =buildErrors(HttpStatus.BAD_REQUEST,"Validation failed",webRequest);
        body.put("FieldError",errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handelGeneral(Exception exception, WebRequest webRequest){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrors(HttpStatus.INTERNAL_SERVER_ERROR,"radios server error",webRequest));
    }




}
