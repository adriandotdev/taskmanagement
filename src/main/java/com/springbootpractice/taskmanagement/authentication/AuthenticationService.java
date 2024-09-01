package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.jwtauth.JwtService;
import com.springbootpractice.taskmanagement.config.jwtauth.JwtUserDetail;
import com.springbootpractice.taskmanagement.utils.HttpUnauthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public void register(RegisterRequest request) {

        JwtUserDetail jwtUserDetail = new JwtUserDetail.UserDetailBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(JwtUserDetail.ROLES.USER)
                .build();

        this.repository.register(jwtUserDetail);
    }

    public String login(LoginRequest request) {

        JwtUserDetail jwtUserDetail = this.repository.getUserByUsername(request.username()).orElseThrow(() -> new BadCredentialsException("INVALID_CREDENTIALS"));

        return jwtService.generateToken(jwtUserDetail);
    }
}
