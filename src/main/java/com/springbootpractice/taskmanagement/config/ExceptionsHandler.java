package com.springbootpractice.taskmanagement.config;

import com.springbootpractice.taskmanagement.utils.CustomResponse;
import com.springbootpractice.taskmanagement.utils.HttpBadRequest;
import com.springbootpractice.taskmanagement.utils.HttpUnauthorized;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(HttpUnauthorized.class)
    public ResponseEntity<CustomResponse<?>> handleUnauthorizedException(HttpUnauthorized err) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomResponse<>("Unauthorized", null, err.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseEntity<?>> handleBadCredentialsException(BadCredentialsException err) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseEntity<?>> handleBadCredentialsException(ExpiredJwtException err) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseEntity<>("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED));
    }
}
