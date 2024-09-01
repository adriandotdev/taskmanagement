package com.springbootpractice.taskmanagement.config.basicauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class BasicUserDetail implements UserDetails {

    private final String username;
    private final String password;

    public BasicUserDetail(BasicUserDetailBuilder builder) {

        this.username = builder.username;
        this.password = builder.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class BasicUserDetailBuilder {

        private String username;
        private String password;

        public BasicUserDetailBuilder setUsername(String username) {

            if (username.isEmpty()) throw new RuntimeException("Username is empty");

            this.username = username;

            return this;
        }

        public BasicUserDetailBuilder setPassword(String password) {

            if (password.isEmpty()) throw new RuntimeException("Password is empty");

            this.password = password;

            return this;
        }

        public BasicUserDetail build() {

            if (this.username.isEmpty() || this.password.isEmpty()) throw new RuntimeException("Basic User Detail cannot build");

            return new BasicUserDetail(this);
        }
    }
}
