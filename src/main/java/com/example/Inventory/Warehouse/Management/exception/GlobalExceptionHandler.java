package com.example.Inventory.Warehouse.Management.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
@RestControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<ExceptionResponse>handleNotFoundException(ResourceNotFoundException exception){
         ExceptionResponse response=new ExceptionResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage(),LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
     }

     @ExceptionHandler(ResourceAlreadyExistsException.class)
     public ResponseEntity<ExceptionResponse>handleDuplicateResourceException(ResourceAlreadyExistsException exception){
         ExceptionResponse response=new ExceptionResponse(HttpStatus.CONFLICT.value(), exception.getMessage(), LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
     }
     @ExceptionHandler(BadRequestException.class)
     public ResponseEntity<ExceptionResponse>handleBadRequest(BadRequestException exception){
         ExceptionResponse response=new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
     }
     //suppose user send Get by id but no such method in endpoint
     @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
     public ResponseEntity<ExceptionResponse>handleRequestNotSupportException(HttpRequestMethodNotSupportedException exception){
         ExceptionResponse response=new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage(), LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
     }
     //pathVariable,Query param dataType missMatch
     @ExceptionHandler(MethodArgumentTypeMismatchException.class)
     public ResponseEntity<ExceptionResponse>handleMethodInvalidException(MethodArgumentTypeMismatchException exception){
         ExceptionResponse response=new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
     }
     //for input mismatch like Double x="ok"
    //Req body data Type mismatch
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(HttpMessageNotReadableException exception) {

        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request data. Please check the provided values.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionResponse> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.CONFLICT.value(),
                "Invalid request data. Please check the provided values.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
//when @valid fails
     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse>handleInvalidInputRequest(MethodArgumentNotValidException exception){
         String message = exception.getBindingResult()
                 .getFieldErrors()
                 .stream()
                 .map(error -> error.getDefaultMessage())
                 .findFirst()
                 .orElse("Validation failed");
         ExceptionResponse response=new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),message,LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

     }
}
