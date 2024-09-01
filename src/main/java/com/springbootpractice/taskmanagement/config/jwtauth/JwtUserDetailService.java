package com.springbootpractice.taskmanagement.config.jwtauth;

import com.springbootpractice.taskmanagement.authentication.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private AuthenticationRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
    }
}
