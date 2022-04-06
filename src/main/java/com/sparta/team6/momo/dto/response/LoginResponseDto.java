package com.sparta.team6.momo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team6.momo.dto.TokenDto;
import lombok.Builder;
import lombok.Data;
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
