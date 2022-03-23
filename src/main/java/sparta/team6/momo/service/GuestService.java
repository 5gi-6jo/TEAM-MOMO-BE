package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.model.Account;
import sparta.team6.momo.model.UserRole;
import sparta.team6.momo.model.UserRole.Authority;
import sparta.team6.momo.repository.AccountRepository;
import sparta.team6.momo.security.auth.MoMoUser;
import sparta.team6.momo.security.jwt.TokenProvider;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static sparta.team6.momo.model.UserRole.ROLE_GUEST;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto connectGuest(String nickname) {
        //TODO: GUEST 유저 강제 회원가입
        Account account = new Account("TODO", "TODO", nickname, ROLE_GUEST);
        Account savedGuest = accountRepository.save(account);


        MoMoUser principal = new MoMoUser(savedGuest.getId(), Collections.singleton(new SimpleGrantedAuthority(Authority.GUEST)));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenDto.getRefreshToken(), tokenProvider.getREFRESH_TOKEN_VALIDITY(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }
}
