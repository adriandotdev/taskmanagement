package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.jwtauth.JwtUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        JwtUserDetail jwtUserDetail = new JwtUserDetail.UserDetailBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(JwtUserDetail.ROLES.USER)
                .build();

        this.repository.register(jwtUserDetail);
    }

    public Optional<JwtUserDetail> getUserByUsername(String username) {

        return this.repository.getUserByUsername(username);
    }
}
