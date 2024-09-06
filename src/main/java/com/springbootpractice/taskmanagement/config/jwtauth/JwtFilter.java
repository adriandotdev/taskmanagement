package com.springbootpractice.taskmanagement.config.jwtauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootpractice.taskmanagement.config.basicauth.CustomFilter;
import com.springbootpractice.taskmanagement.utils.CustomResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger =  LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var type = authHeader.split(" ")[0];
        var token = authHeader.split(" ")[1];

        if (!type.equals("Bearer") || token.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        String username = "";

        try {
            username = jwtService.extractUsername(token);
        }
        catch(ExpiredJwtException e) {
            logger.error("JWT_TOKEN_EXPIRED");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(   mapper.writeValueAsString(new CustomResponse<>("EXPIRED_TOKEN", null, e.getMessage())));
        }
        catch (Exception e) {
            logger.error("INVALID_TOKEN");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(   mapper.writeValueAsString(new CustomResponse<>("INVALID_TOKEN", null, e.getMessage())));
        }

        logger.info("JWT HERE");
        logger.info("USERNAME: {}", username);
        logger.info("AUTHENTICATION CONTEXT: {}", SecurityContextHolder.getContext().getAuthentication());
        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

            logger.info("VALID JWT");
            JwtUserDetail jwtUserDetail = (JwtUserDetail) this.userDetailsService.loadUserByUsername(username);

            System.out.println("JWT USER DETAIL: " + jwtUserDetail.getUsername());
            System.out.println(jwtService.isTokenValid(jwtUserDetail, token));

            if (jwtService.isTokenValid(jwtUserDetail, token)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwtUserDetail, null, jwtUserDetail.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
