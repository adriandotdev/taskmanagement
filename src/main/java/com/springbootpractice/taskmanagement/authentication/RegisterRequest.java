package com.springbootpractice.taskmanagement.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        String givenName,

        @NotBlank
        String middleName,

        @NotBlank
        String lastName,

        @NotBlank
        @Size(min = 8, message = "Username must have at least length of 8")
        String username,

        @NotBlank
        @Size(min = 8, message = "Password must have at least length of 8")
        String password
) {
}
