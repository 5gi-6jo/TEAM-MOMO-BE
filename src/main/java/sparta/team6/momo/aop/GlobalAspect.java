package sparta.team6.momo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import sparta.team6.momo.dto.Success;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.DefaultException;

import static sparta.team6.momo.exception.ErrorCode.ONLY_LOGOUT_ACCESS;

@Aspect
@Slf4j
@Component
public class GlobalAspect {

    @Pointcut("execution(public * sparta.team6.momo.controller.UserController.getUserInfo(..))")
    private void getUserInfo(){}

    @Before("@annotation(sparta.team6.momo.annotation.LogoutCheck)")
    public void onlyLogoutAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            throw new CustomException(ONLY_LOGOUT_ACCESS);
        }
    }

    @Around("@annotation(sparta.team6.momo.annotation.DTOValid)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors e = (Errors) arg;
                if (e.hasErrors())
                    throw new DefaultException(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage());
            }
        }
        return joinPoint.proceed();
    }

}
