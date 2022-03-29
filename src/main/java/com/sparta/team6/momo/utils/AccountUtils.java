package com.sparta.team6.momo.utils;

import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.sparta.team6.momo.repository.AccountRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountUtils {

    private final AccountRepository accountRepository;

    public long getCurUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new CustomException(ErrorCode.ONLY_LOGIN_ACCESS);
        return Long.parseLong(authentication.getName());
    }

    public String getCurNickname() {
        long userId = getCurUserId();
        Optional<Account> user = accountRepository.findById(userId);
        return user.map(Account::getNickname).orElse(null);
    }
}
