package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.JwtService;
import com.springbootpractice.taskmanagement.config.UserDetail;
import com.springbootpractice.taskmanagement.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<?>> register(@RequestBody RegisterRequest request) {

        this.service.register(request);

        return new ResponseEntity<>(new CustomResponse<>("Created", List.of(), "Success"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody LoginRequest request) {

        UserDetail user = this.service.getUserByUsername(request.username()).orElseThrow(() -> new RuntimeException("Not Found"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new CustomResponse<>("OK", token, "Success"));
    }
}
