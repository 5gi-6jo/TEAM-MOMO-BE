package sparta.team6.momo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.model.User;
import sparta.team6.momo.repository.UserRepository;
import sparta.team6.momo.security.auth.MoMoUser;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //임시
    private final UserRepository userRepository;

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
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto requestDto, BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(Fail.of(bindingResult));

        TokenDto jwt = userService.loginUser(requestDto.getEmail(), requestDto.getPassword());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", jwt.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(6000000)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken())
                .body(new Success<>());
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
        userService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return ResponseEntity.ok().body(new Success<>());
    }

    @GetMapping("/login")
    public ResponseEntity<?> getUserInfo(Authentication authentication,  @CookieValue(name = "refresh_token", defaultValue = "refresh") String cookie) {
        if (authentication == null)
            return ResponseEntity.ok().body(new Success<>());
        System.out.println("authentication.getName() = " + authentication.getName());
        Optional<User> findUser = userRepository.findById(Long.parseLong(authentication.getName()));
        UserResponseDto userInfo = userService.getUserInfo(findUser.get().getEmail());
        return ResponseEntity.ok().body(Success.of(userInfo));
    }

}
