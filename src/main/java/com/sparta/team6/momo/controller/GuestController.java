package com.sparta.team6.momo.controller;

import com.sparta.team6.momo.dto.GuestRequestDto;
import com.sparta.team6.momo.dto.Success;
import com.sparta.team6.momo.dto.TokenDto;
import com.sparta.team6.momo.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.sparta.team6.momo.security.jwt.JwtFilter;

@RestController
@RequestMapping("/guests")
@RequiredArgsConstructor
@Slf4j
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<Object> connectGuest(@RequestBody GuestRequestDto requestDto) {
        TokenDto tokenDto = guestService.connectGuest(requestDto.getNickname());
        return ResponseEntity.ok()
                .header(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken())
                .body(new Success<>("게스트 로그인 성공"));
    }
}
