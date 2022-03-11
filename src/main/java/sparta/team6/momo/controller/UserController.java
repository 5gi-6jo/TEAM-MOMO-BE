package sparta.team6.momo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        if (bindingResult.hasErrors()) {
            Fail fail = new Fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(fail);
        }

        userService.registerUser(requestDto);
        return ResponseEntity.ok().body(new Success<>());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto requestDto) {
        String jwt = userService.loginUser(requestDto.getEmail(), requestDto.getPassword());
        TokenDto token = new TokenDto(jwt);
        Success<TokenDto> success = new Success<>(token);
        return ResponseEntity.ok().header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt).body(success);
    }

}
