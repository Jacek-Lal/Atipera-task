package com.jacek.atipera_task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
final class GlobalExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    ResponseEntity<?> handleUserNotFound(UserNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "message", "User does not exist"));
    }
}