package com.sparta.team6.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class ReissueResponseDto {
    private TokenDto tokenDto;
    private ResponseCookie cookie;
}
