package com.springbootpractice.taskmanagement.config.basicauth;

import com.springbootpractice.taskmanagement.authentication.AuthenticationRepository;
import com.springbootpractice.taskmanagement.utils.HttpUnauthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasicAuthUserDetailService implements UserDetailsService {

    @Autowired
    public AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.authenticationRepository.getBasicTokenByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USERNAME NOT FOUND"));
    }
}
