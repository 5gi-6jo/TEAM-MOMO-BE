package sparta.team6.momo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sparta.team6.momo.exception.CustomException;

import static sparta.team6.momo.exception.ErrorCode.ONLY_LOGOUT_ACCESS;

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
        if (auth.getPrincipal() instanceof UserDetails) {
            throw new CustomException(ONLY_LOGOUT_ACCESS);
        }
    }
}
