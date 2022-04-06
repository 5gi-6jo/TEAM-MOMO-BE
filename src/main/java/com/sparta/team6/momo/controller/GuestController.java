package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.GuestRequestDto;
import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.dto.TokenDto;
import com.sparta.team6.momo.security.jwt.TokenUtils;
import com.sparta.team6.momo.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.team6.momo.security.jwt.TokenInfo.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;
    private final TokenUtils tokenUtils;

    // 게스트 생성
    @PostMapping
    public ResponseEntity<Success<Object>> connectGuest(@RequestBody GuestRequestDto requestDto) {
        TokenDto tokenDto = guestService.connectGuest(requestDto.getNickname());
        ResponseCookie cookie = tokenUtils.createTokenCookie(tokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(AUTHORIZATION_HEADER, tokenDto.getAccessToken())
                .body(new Success<>("게스트 로그인 성공"));
    }
}
