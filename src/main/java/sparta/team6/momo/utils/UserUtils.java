package sparta.team6.momo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.model.User;
import sparta.team6.momo.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

import static sparta.team6.momo.exception.ErrorCode.ONLY_LOGIN_ACCESS;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public long getCurUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new CustomException(ONLY_LOGIN_ACCESS);
        return Long.parseLong(authentication.getName());
    }

    public String getCurNickname() {
        long userId = getCurUserId();
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getNickname).orElse(null);
    }
}
