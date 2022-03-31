package com.sparta.team6.momo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.team6.momo.annotation.DTOValid;
import com.sparta.team6.momo.annotation.LogoutCheck;
import com.sparta.team6.momo.dto.*;
import com.sparta.team6.momo.service.AccountService;
import com.sparta.team6.momo.service.OAuthService;
import com.sparta.team6.momo.utils.AccountUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.sparta.team6.momo.security.jwt.JwtFilter;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AccountService accountService;
    private final OAuthService oAuthService;
    private final AccountUtils accountUtils;


    // 회원가입
    @Operation(summary = "회원가입", description = "")
    @PostMapping("/signup")
    @LogoutCheck
    @DTOValid
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        accountService.registerUser(requestDto);
        return ResponseEntity.ok().body(new Success<>("회원가입 성공"));
    }


    // 로그인
    @PostMapping("/login")
    @LogoutCheck
    @DTOValid
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto requestDto, BindingResult bindingResult) {
        TokenDto jwt = accountService.loginUser(requestDto.getEmail(), requestDto.getPassword());
        String nickname = accountService.getNickname(requestDto.getEmail());
        ResponseCookie cookie = createTokenCookie(jwt.getRefreshToken());


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken())
                .body(new Success<>("로그인 성공", new LoginResponseDto(nickname)));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody TokenDto tokenDto,
            @CookieValue(name = "refresh_token") String refreshToken) {

        accountService.logout(tokenDto.getAccessToken(), refreshToken);
        return ResponseEntity.ok().body(new Success<>());
    }

    // 토큰 재발행
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(
            @RequestBody TokenDto tokenDto,
            @CookieValue(name = "refresh_token") String refreshToken) {

        TokenDto reissueTokenDto = accountService.reissue(tokenDto.getAccessToken(), refreshToken);
        ResponseCookie cookie = createTokenCookie(reissueTokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, reissueTokenDto.getAccessToken())
                .body(new Success<>());
    }

    //로그인 유저 정보
    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        log.info("ok");
        AccountResponseDto userInfo = accountService.getUserInfo(accountUtils.getCurUserId());
        return ResponseEntity.ok().body(Success.of(userInfo));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        TokenDto tokenDto = oAuthService.kakaoLogin(code);
        ResponseCookie cookie = createTokenCookie(tokenDto.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken())
                .body(new Success<>());
    }

    //device token 저장
    @PostMapping("/devices")
    public ResponseEntity<Object> updateDeviceToken(@RequestBody @Valid DeviceTokenRequestDto requestDto, BindingResult bindingResult) {
        accountService.updateDeviceToken(requestDto.getToken(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("저장 완료"));
    }

    @PatchMapping("/nicknames")
    public ResponseEntity<Object> updateNickname(@RequestBody @Valid NicknameRequestDto requestDto, BindingResult bindingResult) {
        accountService.updateNickname(requestDto.getNickname(), accountUtils.getCurUserId());
        return ResponseEntity.ok().body(new Success<>("변경 완료"));
    }

    private ResponseCookie createTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(6000000)
                .build();
    }
}
