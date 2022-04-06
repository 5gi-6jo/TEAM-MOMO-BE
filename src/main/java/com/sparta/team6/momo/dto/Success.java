package com.sparta.team6.momo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Success<T> {
    private final boolean success = true;
    private String message = "success";
    private T data;

    public Success(String message) {
        this.message = message;
    }

    public Success(T data) {
        this.data = data;
    }

    public Success(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static Success<UserInfoResponseDto> from(UserInfoResponseDto accountResponseDto) {
        return new Success<>(accountResponseDto);
    }
}

