package com.springbootpractice.taskmanagement.utils;

public class HttpUnauthorized extends RuntimeException {

    public HttpUnauthorized(String message) {
        super(message);
    }
}
