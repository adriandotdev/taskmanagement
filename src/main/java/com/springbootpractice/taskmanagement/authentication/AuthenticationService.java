package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    public Optional<UserDetail> getUserByUsername(String username) {

        return this.repository.getUserByUsername(username);
    }
}
