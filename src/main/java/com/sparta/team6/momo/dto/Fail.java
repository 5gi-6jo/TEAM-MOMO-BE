package com.sparta.team6.momo.dto;

import lombok.Data;
import org.springframework.validation.BindingResult;

@Data
public class Fail {
    private final boolean success = false;
    private String message;

    public Fail(String errorMessage) {
        message = errorMessage;
    }

    public static Fail of(BindingResult bindingResult) {
        return new Fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
    }

}

