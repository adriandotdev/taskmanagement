package com.springbootpractice.taskmanagement.users;

import com.springbootpractice.taskmanagement.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping
    public ResponseEntity<CustomResponse<?>> getUsers() {

        return ResponseEntity.ok(new CustomResponse<>("OK", null, "Users"));
    }
}
