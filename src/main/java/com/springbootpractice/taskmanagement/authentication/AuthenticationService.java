package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.springbootpractice.taskmanagement.authentication.RegisterRequest;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        UserDetail userDetail = new UserDetail.UserDetailBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(UserDetail.ROLES.USER)
                .build();

        this.repository.register(userDetail);
    }

    public Optional<UserDetail> getUserByUsername(String username) {

        return this.repository.getUserByUsername(username);
    }
}
