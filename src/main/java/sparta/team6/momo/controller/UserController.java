package sparta.team6.momo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입
    @Operation(summary = "회원가입", description = "")
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(Fail.of(bindingResult));

        userService.registerUser(requestDto);
        return ResponseEntity.ok().body(new Success<>());
    }


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(Fail.of(bindingResult));

        TokenDto jwt = userService.loginUser(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok().header(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken()).body(Success.of(jwt));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto) {
        userService.logout(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return ResponseEntity.ok().body(new Success<>());
    }

    // 토큰 재발행
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody TokenDto tokenDto) {
        TokenDto token = userService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return ResponseEntity.ok().body(Success.of(token));
    }

    @GetMapping("/login")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.ok().body(new Success<>());
        UserResponseDto userInfo = userService.getUserInfo(authentication.getName());
        return ResponseEntity.ok().body(Success.of(userInfo));
    }

}
