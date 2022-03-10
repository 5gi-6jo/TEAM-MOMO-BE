package sparta.team6.momo.security.oauth;

import antlr.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sparta.team6.momo.security.auth.PrincipalDetails;
import sparta.team6.momo.security.jwt.JwtFilter;
import sparta.team6.momo.security.jwt.TokenProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt = tokenProvider.createToken(authentication);
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jwt);
    }
}
