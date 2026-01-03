package com.jacek.atipera_task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
final class GlobalExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "message", "User does not exist"));
    }
}