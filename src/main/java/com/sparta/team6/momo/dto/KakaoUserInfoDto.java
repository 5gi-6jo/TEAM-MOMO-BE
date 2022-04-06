package com.sparta.team6.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String email;
}
