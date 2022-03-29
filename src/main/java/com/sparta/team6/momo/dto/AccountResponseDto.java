package com.sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.sparta.team6.momo.model.Account;

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


    public static AccountResponseDto of(Account account) {
        return AccountResponseDto.builder()
                .userId(account.getId())
                .email(account.getEmail())
                .nickname(account.getNickname())
                .build();
    }
}
