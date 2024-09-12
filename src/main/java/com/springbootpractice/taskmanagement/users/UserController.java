package com.springbootpractice.taskmanagement.users;

import com.springbootpractice.taskmanagement.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping
    public ResponseEntity<CustomResponse<?>> getUsers() {

        return ResponseEntity.ok(new CustomResponse<>("OK", this.service.GetUsers(), "Users"));
    }
}
