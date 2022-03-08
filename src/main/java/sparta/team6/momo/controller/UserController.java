package sparta.team6.momo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sparta.team6.momo.dto.Fail;
import sparta.team6.momo.dto.SignupRequestDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Fail fail = new Fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(fail);
        }

        userService.registerUser(requestDto);
        return ResponseEntity.ok().body(new Success<>());
    }

}
