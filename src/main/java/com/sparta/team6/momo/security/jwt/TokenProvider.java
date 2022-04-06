package com.sparta.team6.momo.security.jwt;

import com.sparta.team6.momo.dto.TokenDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.model.UserRole;
import com.sparta.team6.momo.security.auth.MoMoUser;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.sparta.team6.momo.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static com.sparta.team6.momo.model.UserRole.Role.USER;


@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {


    private final TokenInfo tokenInfo;
    private final TokenUtils tokenUtils;

    private long ACCESS_TOKEN_VALIDITY;

    @Getter
    private long REFRESH_TOKEN_VALIDITY;

    private long GUSEST_REFRESH_TOKEN_VALIDITY;

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    @Autowired
    public TokenProvider(
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
            @Value("${jwt.guest-refresh-token-validity-in-seconds}") long guestRefreshTokenValidityInSeconds,
            TokenInfo tokenInfo,
            TokenUtils tokenUtils) {
        ACCESS_TOKEN_VALIDITY = accessTokenValidityInSeconds * 1000;
        REFRESH_TOKEN_VALIDITY = refreshTokenValidityInSeconds * 1000;
        GUSEST_REFRESH_TOKEN_VALIDITY = guestRefreshTokenValidityInSeconds * 1000;
        this.tokenInfo = tokenInfo;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void afterPropertiesSet() {
        key = tokenInfo.getKey();
    }


    public TokenDto createToken(Authentication authentication) {
        String accessToken = createAccessToken(authentication);

        boolean isUser = authentication.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals(USER)
        );

        String refreshToken;
        if (isUser) refreshToken = createRefreshToken(REFRESH_TOKEN_VALIDITY);
        else refreshToken = createRefreshToken(GUSEST_REFRESH_TOKEN_VALIDITY);

        return TokenDto.withBearer(accessToken, refreshToken);
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsWithoutExpirationCheck(accessToken);
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesFrom(claims);

        MoMoUser principal = new MoMoUser(Long.parseLong(claims.getSubject()), authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    private String createAccessToken(Authentication authentication) {
        String authorities = getAuthoritiesFrom(authentication);
        String userId = authentication.getName();
        return Jwts.builder()
                .setSubject(userId)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(getExpireDate(ACCESS_TOKEN_VALIDITY))
                .compact();
    }

    private String createRefreshToken(long validity) {
        return Jwts.builder()
                .setExpiration(getExpireDate(validity))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getClaimsWithoutExpirationCheck(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
    }

    private String getAuthoritiesFrom(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFrom(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Date getExpireDate(long expireIn) {
        long now = (new Date()).getTime();
        return new Date(now + expireIn);
    }
}
