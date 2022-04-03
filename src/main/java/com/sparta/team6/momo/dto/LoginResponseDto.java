package com.sparta.team6.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String nickname;
    private boolean isNoticeAllowed;
}
