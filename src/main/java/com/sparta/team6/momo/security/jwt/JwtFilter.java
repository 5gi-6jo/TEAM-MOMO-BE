package com.sparta.team6.momo.security.jwt;

import com.sparta.team6.momo.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.sparta.team6.momo.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static com.sparta.team6.momo.security.jwt.TokenInfo.AUTHORIZATION_HEADER;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final TokenUtils tokenUtils;

    private static final String REISSUE_URL = "/users/reissue";
    private static final String LOGOUT_URL = "/users/logout";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String jwt = tokenUtils.resolveAccessToken(bearerToken);
        String requestURI = request.getRequestURI();


        if (StringUtils.hasText(jwt)) {
            if (!requestURI.equals(REISSUE_URL) && !requestURI.equals(LOGOUT_URL))
                tokenUtils.isTokenValidate(jwt);

            if (tokenUtils.isTokenBlackList(jwt))
                throw new CustomException(INVALID_ACCESS_TOKEN);

            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
