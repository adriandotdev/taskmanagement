package com.springbootpractice.taskmanagement.utils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException err) {

        BindingResult bs = err.getBindingResult();
        HashMap<String, String> errors = new HashMap<>();

        for (FieldError error : bs.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(new CustomResponse<>("Bad Request", errors, "Bad Request"));
    }

    @ExceptionHandler(HttpBadRequest.class)
    public ResponseEntity<CustomResponse<?>> handleBadRequestException(HttpBadRequest err) {

        return ResponseEntity.badRequest().body(
                new CustomResponse<>("Bad Request", null, err.getMessage())
        );
    }

    @ExceptionHandler(HttpUnauthorized.class)
    public ResponseEntity<CustomResponse<?>> handleUnauthorizedException(HttpUnauthorized err) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomResponse<>("Unauthorized", null, err.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse<?>> handleBadCredentialsException(BadCredentialsException err) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomResponse<>(HttpStatus.UNAUTHORIZED.toString(), err.getMessage(), err.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseEntity<?>> handleBadCredentialsException(ExpiredJwtException err) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseEntity<>("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomResponse<?>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.badRequest().body(new CustomResponse<>("BAD REQUEST", null, e.getMessage()));
    }
}
