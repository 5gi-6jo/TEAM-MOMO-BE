package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sparta.team6.momo.dto.SignupRequestDto;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.dto.AccountResponseDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Account;
import sparta.team6.momo.repository.AccountRepository;
import sparta.team6.momo.security.jwt.TokenProvider;
import sparta.team6.momo.security.jwt.TokenUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static sparta.team6.momo.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static sparta.team6.momo.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static sparta.team6.momo.model.UserRole.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {



    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenUtils tokenUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public String registerUser(SignupRequestDto requestDto) {
        duplicateEmailCheck(requestDto);
        Account account = new Account(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()), requestDto.getNickname(), ROLE_USER);
        accountRepository.save(account);
        return account.getNickname();
    }


    public TokenDto loginUser(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return createAndSaveToken(authentication);
    }


    public void logout(String accessToken, String refreshToken) {
        Authentication authentication = getAuthenticationWithCheckToken(accessToken, accessToken, INVALID_ACCESS_TOKEN);

        if (isRefreshTokenNotEquals(refreshToken, authentication))
            throw new CustomException(INVALID_REFRESH_TOKEN);

        redisTemplate.delete(authentication.getName());

        Long expiration = tokenUtils.getRemainExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
   }


    public TokenDto reissue(String accessToken, String refreshToken) {
        Authentication authentication = getAuthenticationWithCheckToken(refreshToken, accessToken, INVALID_REFRESH_TOKEN);

        if (isRefreshTokenNotEquals(refreshToken, authentication))
            throw new CustomException(INVALID_REFRESH_TOKEN);

        return createAndSaveToken(authentication);
    }

    public AccountResponseDto getUserInfo(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다")
        );
        return AccountResponseDto.of(account);
    }

    public String getNickname(String email) {
        Optional<Account> user = accountRepository.findByEmail(email);
        return user.map(Account::getNickname).orElse(null);
    }

    private TokenDto createAndSaveToken(Authentication authentication) {
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenDto.getRefreshToken(), tokenProvider.getREFRESH_TOKEN_VALIDITY(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }


    private Authentication getAuthenticationWithCheckToken(String validateToken, String accessToken, ErrorCode errorCode) {
        if (!tokenUtils.isTokenValidate(validateToken)) {
            throw new CustomException(errorCode);
        }
        return tokenProvider.getAuthentication(accessToken);
    }


    private boolean isRefreshTokenNotEquals(String refreshToken, Authentication authentication) {
        String savedRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        return !refreshToken.equals(savedRefreshToken) || ObjectUtils.isEmpty(savedRefreshToken);
    }


    private void duplicateEmailCheck(SignupRequestDto requestDto) {
        if (accountRepository.findByEmail(requestDto.getEmail()).orElse(null) != null) {
            throw new AccessDeniedException("이미 가입되어 있는 유저입니다.");
        }
    }

}
