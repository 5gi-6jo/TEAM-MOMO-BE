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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Aspect
@Slf4j
@Component
public class GlobalAspect {

    @Pointcut("execution(public * com.sparta.team6.momo.controller.UserController.getUserInfo(..))")
    private void getUserInfo(){}

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
                if (e.hasFieldErrors())
                    throw DefaultException.fromFieldError(HttpStatus.BAD_REQUEST, e.getFieldError());
            }
        }
        return joinPoint.proceed();
    }

}
