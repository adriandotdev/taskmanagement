package com.springbootpractice.taskmanagement.config.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize.
                                        requestMatchers("/api/v1/auth/**").
                                        authenticated()
                                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasicConfigurer -> {
                    httpBasicConfigurer.authenticationEntryPoint(new BasicAuthEntryPoint());
                });


        return http.build();
    }
}
