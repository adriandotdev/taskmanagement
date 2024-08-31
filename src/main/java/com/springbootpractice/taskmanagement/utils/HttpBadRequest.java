package com.springbootpractice.taskmanagement.utils;

public class HttpBadRequest extends RuntimeException {

    public HttpBadRequest(String message) {
        super(message);
    }
}
