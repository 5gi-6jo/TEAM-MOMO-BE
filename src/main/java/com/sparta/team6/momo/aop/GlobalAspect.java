package com.sparta.team6.momo.aop;

import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.DefaultException;
import com.sparta.team6.momo.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Aspect
@Slf4j
@Component
public class GlobalAspect {

    @Before("@annotation(com.sparta.team6.momo.annotation.LogoutCheck)")
    public void onlyLogoutAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            throw new CustomException(ErrorCode.ONLY_LOGOUT_ACCESS);
        }
    }

    @Around("@annotation(com.sparta.team6.momo.annotation.DTOValid)")
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
