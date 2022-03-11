package sparta.team6.momo.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.dto.TokenDto;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.security.jwt.TokenProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt = tokenProvider.createToken(authentication);
        setResponseWithJwt(response, jwt);
    }

    private void setResponseWithJwt(HttpServletResponse response, String jwt) throws IOException {
        setHeader(response, jwt);
        setBody(response, jwt);
    }

    private void setHeader(HttpServletResponse response, String jwt) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
    }

    private void setBody(HttpServletResponse response, String jwt) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Success<TokenDto> success = Success.tokenDtoSuccess(jwt);
        response.getWriter().write(objectMapper.writeValueAsString(success));
    }
}
