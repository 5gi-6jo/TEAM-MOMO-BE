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

    public Long getPlanId(String url) {
        Optional<Plan> plan = planRepository.findPlanByUrl(url);
        return 0L;
//        return plan.map(Plan::getId).orElseThrow(new CustomException(INVALID_MAP_URL));
    }

    public String createRandomUrl() {
        String prefix = "https://www.seoultaste.click/map/";
        return prefix + UUID.randomUUID();
    }
}
