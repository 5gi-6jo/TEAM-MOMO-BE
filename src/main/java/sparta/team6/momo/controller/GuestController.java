package sparta.team6.momo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sparta.team6.momo.dto.GuestRequestDto;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.service.GuestService;

@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<?> connectGuest(@RequestBody GuestRequestDto requestDto) {
        TokenDto tokenDto = guestService.connectGuest(requestDto.getNickname());
        return ResponseEntity.ok()
                .header(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken())
                .body(new Success<>());
    }

    @GetMapping
    public void test(Authentication authentication) {
        System.out.println(authentication);
    }
}
