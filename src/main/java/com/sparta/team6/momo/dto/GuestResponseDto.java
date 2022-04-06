package com.sparta.team6.momo.dto;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor
public class GuestResponseDto {
    private TokenDto tokenDto;
    private ResponseCookie cookie;
}
