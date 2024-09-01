package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.jwtauth.JwtService;
import com.springbootpractice.taskmanagement.config.jwtauth.JwtUserDetail;
import com.springbootpractice.taskmanagement.utils.CustomResponse;
import com.springbootpractice.taskmanagement.utils.HttpUnauthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<?>> register(@RequestBody RegisterRequest request) {

        this.service.register(request);

        return new ResponseEntity<>(new CustomResponse<>("Created", List.of(), "Success"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(new CustomResponse<>("OK", this.service.login(request), "Success"));
    }
}
