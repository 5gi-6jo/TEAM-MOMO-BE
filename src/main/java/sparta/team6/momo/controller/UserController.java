package sparta.team6.momo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.annotation.DTOValid;
import sparta.team6.momo.annotation.LogoutCheck;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.service.OAuthService;
import sparta.team6.momo.service.UserService;
import sparta.team6.momo.utils.UserUtils;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final UserUtils userUtils;

    // 회원가입
    @Operation(summary = "회원가입", description = "")
    @PostMapping("/signup")
    @LogoutCheck @DTOValid
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        userService.registerUser(requestDto);
        return ResponseEntity.ok().body(new Success<>());
    }


    // 로그인
    @PostMapping("/login")
    @LogoutCheck @DTOValid
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto requestDto, BindingResult bindingResult) {
        TokenDto jwt = userService.loginUser(requestDto.getEmail(), requestDto.getPassword());
        ResponseCookie cookie = createTokenCookie(jwt.getRefreshToken());


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken())
                .body(new Success<>());
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody TokenDto tokenDto,
            @CookieValue(name = "refresh_token") String refreshToken) {

        userService.logout(tokenDto.getAccessToken(), refreshToken);
        return ResponseEntity.ok().body(new Success<>());
    }

    // 토큰 재발행
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(
            @RequestBody TokenDto tokenDto,
            @CookieValue(name = "refresh_token") String refreshToken) {

        TokenDto reissueTokenDto = userService.reissue(tokenDto.getAccessToken(), refreshToken);
        ResponseCookie cookie = createTokenCookie(reissueTokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, reissueTokenDto.getAccessToken())
                .body(new Success<>());
    }

    @GetMapping("/login")
    public ResponseEntity<?> getUserInfo() {
        log.info("ok");
        UserResponseDto userInfo = userService.getUserInfo(userUtils.getCurUserId());
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


    private ResponseCookie createTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(6000000)
                .build();
    }

}
