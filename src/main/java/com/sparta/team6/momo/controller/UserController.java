package com.sparta.team6.momo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.team6.momo.annotation.DTOValid;
import com.sparta.team6.momo.annotation.LogoutCheck;
import com.sparta.team6.momo.dto.request.DeviceTokenRequestDto;
import com.sparta.team6.momo.dto.request.LoginRequestDto;
import com.sparta.team6.momo.dto.request.NicknameRequestDto;
import com.sparta.team6.momo.dto.request.SignupRequestDto;
import com.sparta.team6.momo.dto.response.LoginResponseDto;
import com.sparta.team6.momo.dto.response.ReissueResponseDto;
import com.sparta.team6.momo.dto.response.Success;
import com.sparta.team6.momo.dto.response.UserInfoResponseDto;
import com.sparta.team6.momo.service.OAuthService;
import com.sparta.team6.momo.service.UserService;
import com.sparta.team6.momo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.sparta.team6.momo.security.jwt.TokenInfo.REFRESH_TOKEN_COOKIE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final AccountUtils accountUtils;


    // 회원가입
    @LogoutCheck @DTOValid
    @PostMapping("/signup")
    public ResponseEntity<Success<Object>> register(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        userService.register(requestDto.getEmail(), requestDto.getPassword(), requestDto.getNickname());
        return ResponseEntity.ok().body(new Success<>("회원가입 성공"));
    }


    // 로그인
    @LogoutCheck @DTOValid
    @PostMapping("/login")
    public ResponseEntity<Success<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto requestDto, BindingResult bindingResult) {
        LoginResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());

        return ResponseEntity.ok()
                .header(SET_COOKIE, responseDto.getCookie().toString())
                .header(AUTHORIZATION, responseDto.getAccessToken())
                .body(new Success<>("로그인 성공", responseDto));
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Success<Object>> logout(
            @RequestHeader(AUTHORIZATION) String bearerToken,
            @CookieValue(REFRESH_TOKEN_COOKIE) String refreshToken) {

        userService.logout(bearerToken, refreshToken);
        return ResponseEntity.ok().body(new Success<>("로그아웃 성공"));
    }


    // 토큰 재발행
    @GetMapping("/reissue")
    public ResponseEntity<Success<Object>> reissueToken(
            @RequestHeader(AUTHORIZATION) String bearerToken,
            @CookieValue(REFRESH_TOKEN_COOKIE) String refreshToken) {

        ReissueResponseDto responseDto = userService.reissueToken(bearerToken, refreshToken);
        return ResponseEntity.ok()
                .header(SET_COOKIE, responseDto.getCookie().toString())
                .header(AUTHORIZATION, responseDto.getTokenDto().getAccessToken())
                .body(new Success<>("토큰 재발행 성공"));
    }


    // 유저 정보
    @GetMapping
    public ResponseEntity<Success<UserInfoResponseDto>> getUserInfo() {
        UserInfoResponseDto responseDto = userService.getUserInfo(accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("유저 정보 조회 성공", responseDto));
    }


    //device token 저장
    @PostMapping("/devices")
    public ResponseEntity<Object> updateDeviceToken(@RequestBody @Valid DeviceTokenRequestDto requestDto, BindingResult bindingResult) {
        userService.updateDeviceToken(requestDto.getToken(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("저장 완료"));
    }


    @DTOValid
    @PatchMapping("/nicknames")
    public ResponseEntity<Success<Object>> updateNickname(@RequestBody @Valid NicknameRequestDto requestDto, BindingResult bindingResult) {
        userService.updateNickname(requestDto.getNickname(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("닉네임 변경 완료"));
    }


    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<Success<LoginResponseDto>> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        LoginResponseDto responseDto = oAuthService.kakaoLogin(code);

        return ResponseEntity.ok()
                .header(SET_COOKIE, responseDto.getCookie().toString())
                .header(AUTHORIZATION, responseDto.getAccessToken())
                .body(new Success<>("카카오 로그인 성공", responseDto));
    }
}
