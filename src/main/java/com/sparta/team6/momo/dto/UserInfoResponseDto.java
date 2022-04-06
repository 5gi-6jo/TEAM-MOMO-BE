package com.sparta.team6.momo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team6.momo.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDto {
    private Long userId;
    private String email;
    private String nickname;

    @JsonProperty("isNoticeAllowed")
    private boolean noticeAllowed;

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .noticeAllowed(user.isNoticeAllowed())
                .build();
    }
}
