package sparta.team6.momo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static sparta.team6.momo.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {


    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long accessTokenExpireIn;
    private final long refreshTokenExpireIn;

    private final RedisTemplate<String, String> redisTemplate;

    private Key key;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenExpireInSeconds,
            RedisTemplate<String, String> redisTemplate) {
        this.secret = secret;
        accessTokenExpireIn = accessTokenValidityInSeconds * 1000;
        refreshTokenExpireIn = refreshTokenExpireInSeconds * 1000;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public TokenDto createToken(Authentication authentication) {
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken();
        return TokenDto.withBearer(accessToken, refreshToken);
    }


    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        return new UsernamePasswordAuthenticationToken(principal, token, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private Claims getClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public boolean isTokenValid(String jwt) {
        return ObjectUtils.isEmpty(redisTemplate.opsForValue().get(jwt));
    }


    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String createAccessToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);
        String email = authentication.getName();
        return createAccessTokenWith(email, authorities);
    }

    private String createAccessTokenWith(String email, String authorities) {
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(getExpireDate(accessTokenExpireIn))
                .compact();
    }

    private String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(getExpireDate(refreshTokenExpireIn))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Date getExpireDate(long expireIn) {
        long now = (new Date()).getTime();
        return new Date(now + expireIn);
    }
}
