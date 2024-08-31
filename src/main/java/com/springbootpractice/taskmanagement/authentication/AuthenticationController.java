package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.UserDetail;
import com.springbootpractice.taskmanagement.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody LoginRequest request) {

        UserDetail user = this.service.getUserByUsername(request.username()).orElseThrow(() -> new RuntimeException("Not Found"));

        return ResponseEntity.ok(new CustomResponse<>("OK", user, "Success"));
    }
}
