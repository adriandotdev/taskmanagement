package com.springbootpractice.taskmanagement.authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @PostMapping("/register")
    public String register() {

        return "Register";
    }
}
