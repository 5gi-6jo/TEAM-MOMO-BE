package com.sparta.team6.momo.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseDefault(DefaultException defaultException) {
        return ResponseEntity
                .status(defaultException.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(defaultException.getHttpStatus().value())
                        .error(defaultException.getHttpStatus().name())
                        .code(defaultException.getClass().getSimpleName())
                        .message(defaultException.getMessage())
                        .build()
                );
    }

}
