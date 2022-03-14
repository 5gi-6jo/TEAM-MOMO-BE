package sparta.team6.momo.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isTokenBlackList(String jwt) {
        return !ObjectUtils.isEmpty(redisTemplate.opsForValue().get(jwt));
    }
}
