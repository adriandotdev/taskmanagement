package com.springbootpractice.taskmanagement.users;

import com.springbootpractice.taskmanagement.utils.HttpBadRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetail implements org.springframework.security.core.userdetails.UserDetails {

    private final String givenName;
    private final String middleName;
    private final String lastName;
    private final String username;
    private final String password;
    private final ROLES role;

    private enum ROLES {
        ADMIN,
        USER
    }

    private UserDetail(UserRegisterDetailsBuilder builder) {
        this.givenName = builder.givenName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
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

    private static class UserRegisterDetailsBuilder {

        private String givenName;
        private String middleName;
        private String lastName;
        private String username;
        private String password;

        private ROLES role;

        public UserRegisterDetailsBuilder setGivenName(String givenName) {
            if (givenName.isEmpty()) {
                throw new HttpBadRequest("Given name must not be empty");
            }

            this.givenName = givenName;

            return this;
        }

        public UserRegisterDetailsBuilder setMiddleName(String middleName) {

            if (middleName.isEmpty()) throw new HttpBadRequest("Middle name must not be empty");

            this.middleName = middleName;

            return this;
        }

        public UserRegisterDetailsBuilder setLastName(String lastName) {
            if (lastName.isEmpty()) throw new HttpBadRequest("Last name must not be empty");

            this.lastName = lastName;

            return this;
        }

        public UserRegisterDetailsBuilder setUsername(String username) {
            if (username.isEmpty()) throw new HttpBadRequest("Username must not be empty");

            this.username = username;

            return this;
        }

        public UserRegisterDetailsBuilder setPassword(String password) {
            if (password.isEmpty()) throw new HttpBadRequest("Password must not be empty");

            this.password = password;

            return this;
        }

        public UserRegisterDetailsBuilder setRole(ROLES role) {

            this.role = role;

            return this;
        }

        public UserDetail build() {
            if (this.givenName.isEmpty()) throw new HttpBadRequest("UserRegisterDetails instance cannot be build because givenName is empty");

            if (this.lastName.isEmpty()) throw new HttpBadRequest("UserRegisterDetails instance cannot be build because lastName is empty");

            return new UserDetail(this);
        }
    }
}
