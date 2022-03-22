package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;

import java.util.UUID;

import static sparta.team6.momo.exception.ErrorCode.INVALID_MAP_URL;

@Service
@RequiredArgsConstructor
public class MapService {

    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Long> redisTemplate;

    public Long getPlanId(String url) {
        Long planId = redisTemplate.opsForValue().get(url);

        if (planId == null)
            throw new CustomException(INVALID_MAP_URL);

        return planId;
    }

    public void createMapRoom(Long planId) {

    }

    public void createRandomUrl(Long planId) {
        String randomUrl = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(randomUrl, planId);
    }
}
