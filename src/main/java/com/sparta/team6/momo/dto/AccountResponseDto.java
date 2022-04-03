package com.sparta.team6.momo.dto;

import com.sparta.team6.momo.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseDto {
    private Long userId;
    private String email;
    private String nickname;

    @Builder
    public AccountResponseDto(Long userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }


    public static AccountResponseDto from(User user) {
        return AccountResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
