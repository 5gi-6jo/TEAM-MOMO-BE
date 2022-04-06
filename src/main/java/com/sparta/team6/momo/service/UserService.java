package com.sparta.team6.momo.service;


import com.sparta.team6.momo.dto.TokenDto;
import com.sparta.team6.momo.dto.response.LoginResponseDto;
import com.sparta.team6.momo.dto.response.ReissueResponseDto;
import com.sparta.team6.momo.dto.response.UserInfoResponseDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.model.Account;
import com.sparta.team6.momo.model.User;
import com.sparta.team6.momo.repository.AccountRepository;
import com.sparta.team6.momo.repository.UserRepository;
import com.sparta.team6.momo.security.jwt.TokenProvider;
import com.sparta.team6.momo.security.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.sparta.team6.momo.exception.ErrorCode.*;
import static com.sparta.team6.momo.model.Provider.KAKAO;
import static com.sparta.team6.momo.model.Provider.MOMO;
import static com.sparta.team6.momo.model.UserRole.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {


    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final AccountRepository accountRepository;

    @Transactional
    public void register(String email, String password, String nickname) {
        duplicateEmailCheck(email);
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .userRole(ROLE_USER)
                .provider(MOMO)
                .build();
        userRepository.save(user);
    }

    public LoginResponseDto login(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        TokenDto tokenDto = createAndSaveToken(authentication);
        ResponseCookie cookie = tokenUtils.createTokenCookie(tokenDto.getRefreshToken());

        return LoginResponseDto.builder()
                .nickname(user.getNickname())
                .noticeAllowed(user.isNoticeAllowed())
                .tokenDto(tokenDto)
                .cookie(cookie)
                .build();
    }

    @Transactional
    public void logout(String bearerToken, String refreshToken) {
        String accessToken = tokenUtils.resolveAccessToken(bearerToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (isRefreshTokenNotEquals(refreshToken, authentication))
            throw new CustomException(INVALID_REFRESH_TOKEN);

        redisTemplate.delete(authentication.getName());

        Long expiration = tokenUtils.getRemainExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        User user = userRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        user.setLoginFalse();
    }


    public ReissueResponseDto reissueToken(String bearerToken, String refreshToken) {
        String accessToken = tokenUtils.resolveAccessToken(bearerToken);
        Authentication authentication = getAuthenticationWithCheckToken(refreshToken, accessToken);

        if (isRefreshTokenNotEquals(refreshToken, authentication))
            throw new CustomException(INVALID_REFRESH_TOKEN);

        TokenDto tokenDto = createAndSaveToken(authentication);
        ResponseCookie cookie = tokenUtils.createTokenCookie(tokenDto.getRefreshToken());

        return new ReissueResponseDto(tokenDto, cookie);
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserInfoResponseDto::from).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public void updateDeviceToken(String token, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        if (token.equals("")) {
            user.changeNoticeAllowed();
            return;
        }
        user.updateToken(token);
    }

    @Transactional
    public void updateAlarm(Long accountId) {
        User user = userRepository.findById(accountId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        user.changeNoticeAllowed();
    }

    @Transactional
    public void updateNickname(String nickname, Long accountId) {
        Account savedAccount = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        savedAccount.updateNickname(nickname);
    }

    public TokenDto createAndSaveToken(Authentication authentication) {
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenDto.getRefreshToken(), tokenProvider.getREFRESH_TOKEN_VALIDITY(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }


    private Authentication getAuthenticationWithCheckToken(String validateToken, String accessToken) {
        if (!tokenUtils.isTokenValidate(validateToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return tokenProvider.getAuthentication(accessToken);
    }


    private boolean isRefreshTokenNotEquals(String refreshToken, Authentication authentication) {
        String savedRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        return !refreshToken.equals(savedRefreshToken) || ObjectUtils.isEmpty(savedRefreshToken);
    }


    private void duplicateEmailCheck(String email) {
        userRepository.findByEmail(email)
                .ifPresent((user) -> {
                    if (user.getProvider() == KAKAO) throw new CustomException(SAME_EMAIL_OTHER_ACCOUNT_EXIST);
                    else throw new CustomException(DUPLICATE_EMAIL);
                });
    }
}
