package sparta.team6.momo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoginCheckAdvice {

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.registerUser(..))")
    private void register(){}

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.login(..))")
    private void login(){}


    @Before("register() || login()")
    public void onlyLogoutAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getPrincipal().toString());
        if (auth.getPrincipal() instanceof UserDetails) {
            throw new AccessDeniedException("이미 로그인이 되어있습니다");
        }
    }
}
