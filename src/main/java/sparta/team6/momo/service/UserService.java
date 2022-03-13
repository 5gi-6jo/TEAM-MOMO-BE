package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.team6.momo.dto.SignupRequestDto;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.model.User;
import sparta.team6.momo.repository.UserRepository;
import sparta.team6.momo.security.jwt.TokenProvider;

import java.util.concurrent.TimeUnit;

import static sparta.team6.momo.exception.ErrorCode.INVALID_ACCESS_TOKEN;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void registerUser(SignupRequestDto requestDto) {
        duplicateEmailCheck(requestDto);
        User user = new User(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()), requestDto.getNickname());
        userRepository.save(user);
    }


    public TokenDto loginUser(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }


    public void logout(String accessToken, String refreshToken) {
        Authentication authentication = getAuthenticationByToken(accessToken, accessToken);

        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }

        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
   }


    public TokenDto reissue(String accessToken, String refreshToken) {
        Authentication authentication = getAuthenticationByToken(refreshToken, accessToken);
        return tokenProvider.reissueToken(authentication, refreshToken);

    }


    private Authentication getAuthenticationByToken(String validateToken, String accessToken) {
        if (!tokenProvider.validateToken(validateToken)) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }

        return tokenProvider.getAuthentication(accessToken);
    }


    private void duplicateEmailCheck(SignupRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).orElse(null) != null) {
            throw new AccessDeniedException("이미 가입되어 있는 유저입니다.");
        }
    }
}
