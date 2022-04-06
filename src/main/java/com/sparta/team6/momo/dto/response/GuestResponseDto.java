package com.sparta.team6.momo.dto.response;

import com.sparta.team6.momo.dto.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor
public class GuestResponseDto {
    private TokenDto tokenDto;
    private ResponseCookie cookie;
}
