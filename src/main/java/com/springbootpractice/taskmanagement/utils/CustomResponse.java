package com.springbootpractice.taskmanagement.utils;

public record CustomResponse<T>(
        String status,
        T data,
        String message
) {
}
