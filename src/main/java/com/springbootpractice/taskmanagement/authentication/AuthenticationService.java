package com.springbootpractice.taskmanagement.authentication;

import com.springbootpractice.taskmanagement.config.jwtauth.JwtService;
import com.springbootpractice.taskmanagement.config.jwtauth.JwtUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        JwtUserDetail jwtUserDetail = new JwtUserDetail.UserDetailBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRole(JwtUserDetail.ROLES.USER)
                .build();

        int userID = this.repository.register(jwtUserDetail);

        this.repository.insertUserDetails(userID, request);
    }

    public HashMap<String, ?> login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        JwtUserDetail jwtUserDetail = this.repository.getUserByUsername(request.username()).orElseThrow(() -> new BadCredentialsException("INVALID_CREDENTIALS"));

        var accessToken = jwtService.generateToken(jwtUserDetail);
        var refreshToken = jwtService.generateRefreshToken(jwtUserDetail);

        HashMap<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);

        return map;
    }
}
