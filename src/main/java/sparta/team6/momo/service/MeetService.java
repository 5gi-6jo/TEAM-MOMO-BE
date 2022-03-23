package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

import java.util.Optional;
import java.util.UUID;

import static sparta.team6.momo.exception.ErrorCode.INVALID_MAP_URL;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final PlanRepository planRepository;
//    private final String prefix = "http://localhost:3000/planmap/";

    public Long getPlanId(String url) {
        return planRepository.findPlanByUrl(url).
                orElseThrow(() -> new CustomException(INVALID_MAP_URL)).
                getId();
    }

    public String createRandomUrl() {
        return UUID.randomUUID().toString();
    }
}
