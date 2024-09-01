package com.springbootpractice.taskmanagement.config.jwtauth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private JwtUserDetailService jwtUserDetailService;

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

        String username = "";

        try {
            username = jwtService.extractUsername(token);
        }
        catch(ExpiredJwtException err) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT_TOKEN_EXPIRED");
        }
        catch (Exception e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "INVALID_TOKEN");
        }

        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() != null) {

            JwtUserDetail jwtUserDetail = (JwtUserDetail) this.jwtUserDetailService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwtUserDetail, token)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwtUserDetail, null, jwtUserDetail.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
}
