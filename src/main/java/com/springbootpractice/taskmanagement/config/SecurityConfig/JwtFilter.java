package com.springbootpractice.taskmanagement.config.SecurityConfig;

import com.springbootpractice.taskmanagement.config.JwtService;
import com.springbootpractice.taskmanagement.config.UserDetail;
import com.springbootpractice.taskmanagement.config.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        var type = authHeader.split(" ")[0];
        var token = authHeader.split(" ")[1];

        if (!type.equals("Bearer") || authHeader.isEmpty() || token.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(token);

        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() != null) {

            UserDetail userDetail = (UserDetail) this.userDetailService.loadUserByUsername(username);

            if (jwtService.isTokenValid(userDetail, token)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
}
