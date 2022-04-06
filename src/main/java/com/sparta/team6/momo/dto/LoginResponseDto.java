package com.sparta.team6.momo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team6.momo.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.ResponseCookie;

@Data
@Builder
public class LoginResponseDto {

    private String nickname;

    @JsonProperty(value = "isNoticeAllowed")
    private Boolean noticeAllowed;

    @JsonIgnore // header
    private TokenDto tokenDto;

    @JsonIgnore // cookie
    private ResponseCookie cookie;




}
