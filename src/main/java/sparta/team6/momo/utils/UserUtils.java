package sparta.team6.momo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sparta.team6.momo.exception.CustomException;

import static sparta.team6.momo.exception.ErrorCode.ONLY_LOGIN_ACCESS;

@Component
public class UserUtils {

    public long getCurUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new CustomException(ONLY_LOGIN_ACCESS);
        return Long.parseLong(authentication.getName());
    }
}
