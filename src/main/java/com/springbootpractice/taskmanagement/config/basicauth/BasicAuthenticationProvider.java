package com.springbootpractice.taskmanagement.config.basicauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private BasicAuthUserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            BasicUserDetail userDetails = (BasicUserDetail) userDetailService.loadUserByUsername(authentication.getName());
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        }
        catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid Credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
