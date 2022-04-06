package com.sparta.team6.momo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String accessToken;

    @JsonIgnore // cookie
    private ResponseCookie cookie;

}
