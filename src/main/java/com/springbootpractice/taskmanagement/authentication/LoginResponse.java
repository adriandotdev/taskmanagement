package com.springbootpractice.taskmanagement.authentication;

public record LoginResponse(
        String access_token,
        String refresh_token
) {
}
