package com.springbootpractice.taskmanagement.config.basicauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootpractice.taskmanagement.authentication.AuthenticationRepository;
import com.springbootpractice.taskmanagement.utils.CustomResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomFilter extends OncePerRequestFilter {

    private static final Logger logger =  LoggerFactory.getLogger(CustomFilter.class);

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            var authHeader = request.getHeader("Authorization");

            if (authHeader == null) throw new RuntimeException("INVALID_BASIC_TOKEN");

            var token = authHeader.split(" ")[1];

            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

            String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            Optional<BasicAuth> basicAuth = this.repository.getBasicToken(username);

            logger.info("RAW PASSWORD: {}", password);
            logger.info("ENCODED PASSWORD: {}", passwordEncoder.encode(password));
            logger.info("USERNAME: {}", username);

            basicAuth.orElseThrow(() -> new RuntimeException("INVALID_BASIC_TOKEN"));

            logger.info(passwordEncoder.encode(password));
            logger.info(basicAuth.get().password());

            if (!passwordEncoder.matches(password, basicAuth.get().password())) throw new RuntimeException("INVALID_PASSWORD");

            filterChain.doFilter(request, response);
        }
        catch(RuntimeException e) {
            logger.error(e.getMessage());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(   mapper.writeValueAsString(new CustomResponse<>(e.getMessage(), null, e.getMessage())));
        }
    }
}
