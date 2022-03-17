package sparta.team6.momo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingRequestCookieException;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.DefaultException;

import java.util.Arrays;

import static sparta.team6.momo.exception.ErrorCode.ONLY_LOGOUT_ACCESS;

@Aspect
@Slf4j
@Component
public class LoginCheckAdvice {

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.registerUser(..))")
    private void register(){}

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.login(..))")
    private void login(){}

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.reissueToken(..))")
    private void reissueToken(){}

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.getUserInfo(..))")
    private void getUserInfo(){}

    @Before("register() || login() || reissueToken()")
    public void onlyLogoutAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            throw new CustomException(ONLY_LOGOUT_ACCESS);
        }
    }

    @Around("register() || getUserInfo()")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info(arg.toString());
            if (arg instanceof Errors) {
                Errors e = (Errors) arg;
                if (e.hasErrors())
                    throw new DefaultException(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage());
            }
        }
        return joinPoint.proceed();
    }


}
