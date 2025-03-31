package com.liatrio.exercise.dataService.dto;

public record ApiResponse<T>(T data, long timestamp) {
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, System.currentTimeMillis());
    }
}
