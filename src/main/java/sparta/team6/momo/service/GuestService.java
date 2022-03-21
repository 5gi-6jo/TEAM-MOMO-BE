package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.model.Guest;
import sparta.team6.momo.model.UserRole;
import sparta.team6.momo.repository.GuestRepository;
import sparta.team6.momo.security.auth.MoMoUser;
import sparta.team6.momo.security.jwt.TokenProvider;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto connectGuest(String nickname) {
        Guest saveGuest = guestRepository.save(new Guest(nickname));
        MoMoUser principal = new MoMoUser(saveGuest.getId(), Collections.singleton(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.toString())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenDto.getRefreshToken(), tokenProvider.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }
}
