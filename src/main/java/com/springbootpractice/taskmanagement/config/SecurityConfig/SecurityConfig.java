package com.springbootpractice.taskmanagement.config.SecurityConfig;

import com.springbootpractice.taskmanagement.config.basicauth.BasicAuthEntryPoint;
import com.springbootpractice.taskmanagement.config.basicauth.BasicAuthenticationProvider;
import com.springbootpractice.taskmanagement.config.jwtauth.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private BasicAuthenticationProvider basicAuthenticationProvider;

    @Bean
    @Order(1)
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)

                .securityMatcher("/api/v1/auth/**")
                .authorizeHttpRequests(
                        authorize ->  authorize
                                .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(httpBasicConfigurer -> {
                    httpBasicConfigurer.authenticationEntryPoint(new BasicAuthEntryPoint());
                })
                .exceptionHandling(handler -> handler.authenticationEntryPoint(new BasicAuthEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(basicAuthenticationProvider);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/users/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                    authorize.requestMatchers("/users/")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
