package com.springbootpractice.taskmanagement.config;

import com.springbootpractice.taskmanagement.utils.HttpBadRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetail implements org.springframework.security.core.userdetails.UserDetails {

    private final String username;
    private final String password;
    private final UserDetail.ROLES role;

    public static enum ROLES {
        ADMIN,
        USER
    }

    private UserDetail(UserDetailBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getRole() {
        return this.role.name();
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

    public static class UserDetailBuilder {

        private String username;
        private String password;
        private UserDetail.ROLES role;

        public UserDetailBuilder setUsername(String username) {
            if (username.isEmpty()) throw new HttpBadRequest("Username must not be empty");

            this.username = username;

            return this;
        }

        public UserDetailBuilder setPassword(String password) {
            if (password.isEmpty()) throw new HttpBadRequest("Password must not be empty");

            this.password = password;

            return this;
        }

        public UserDetailBuilder setRole(UserDetail.ROLES role) {

            this.role = role;

            return this;
        }

        public UserDetail build() {
            if (this.username.isEmpty()) throw new HttpBadRequest("UserRegisterDetails instance cannot be build because givenName is empty");

            if (this.password.isEmpty()) throw new HttpBadRequest("UserRegisterDetails instance cannot be build because lastName is empty");

            return new UserDetail(this);
        }
    }
}
