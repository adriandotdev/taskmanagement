package com.springbootpractice.taskmanagement.config;

import com.springbootpractice.taskmanagement.utils.CustomResponse;
import com.springbootpractice.taskmanagement.utils.HttpBadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(HttpBadRequest.class)
    public ResponseEntity<CustomResponse<?>> handleBadRequestException(HttpBadRequest err) {

        return ResponseEntity.badRequest().body(
                new CustomResponse<>("Bad Request", null, err.getMessage())
        );
    }
}
