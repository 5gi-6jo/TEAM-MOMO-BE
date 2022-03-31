package com.sparta.team6.momo.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.team6.momo.exception.ErrorResponse;
import com.sparta.team6.momo.exception.custom.NeedLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setHeader("WWW-Authenticate", "Go to login page and login");
        String body = getLoginExceptionBody();
        response.getWriter().write(body);
    }

    private String getLoginExceptionBody() throws JsonProcessingException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .code(NeedLoginException.class.getSimpleName())
                .message("로그인이 필요합니다")
                .build();
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        return om.writeValueAsString(errorResponse);
    }
}
