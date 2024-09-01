package com.springbootpractice.taskmanagement.authentication;

public record RegisterRequest(

        String givenName,
        String middleName,
        String lastName,
        String username,
        String password
) {
}
