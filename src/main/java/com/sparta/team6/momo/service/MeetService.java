package com.sparta.team6.momo.service;

import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.team6.momo.repository.PlanRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final PlanRepository planRepository;
//    private final String prefix = "http://localhost:3000/planmap/";

    public Long getPlanId(String url) {
        return planRepository.findPlanByUrl(url).
                orElseThrow(() -> new CustomException(ErrorCode.INVALID_MAP_URL)).
                getId();
    }

    public String createRandomUrl() {
        return UUID.randomUUID().toString();
    }
}
