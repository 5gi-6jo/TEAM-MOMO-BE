package sparta.team6.momo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import sparta.team6.momo.dto.TokenDto;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {


    private static final String AUTHORITIES_KEY = "auth";
    private final String SECRET;
    private final long ACCESS_TOKEN_VALIDITY;
    private final long REFRESH_TOKEN_VALIDITY;
    private Key key;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        SECRET = secret;
        ACCESS_TOKEN_VALIDITY = accessTokenValidityInSeconds * 1000;
        REFRESH_TOKEN_VALIDITY = refreshTokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public TokenDto createToken(Authentication authentication) {
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken();
        return TokenDto.withBearer(accessToken, refreshToken);
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsWithoutExpirationCheck(accessToken);

//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        return new UsernamePasswordAuthenticationToken(principal, accessToken, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }


    public boolean isTokenValidate(String token) {
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

    public Long getRemainExpiration(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


    public long getRefreshTokenValidity() {
        return REFRESH_TOKEN_VALIDITY;
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
        }
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
                .setExpiration(getExpireDate(ACCESS_TOKEN_VALIDITY))
                .compact();
    }

    private String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(getExpireDate(REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Date getExpireDate(long expireIn) {
        long now = (new Date()).getTime();
        return new Date(now + expireIn);
    }
}
