package com.sparta.team6.momo.service;

import com.sparta.team6.momo.dto.MeetResponseDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.model.Plan;
import com.sparta.team6.momo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.sparta.team6.momo.exception.ErrorCode.INVALID_MAP_URL;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final PlanRepository planRepository;

    public MeetResponseDto getPlanInfo(String url) {
        Plan plan = planRepository.findPlanByUrl(url).
                orElseThrow(() -> new CustomException(INVALID_MAP_URL));
        return MeetResponseDto.builder()
                .planId(plan.getId())
                .planeName(plan.getPlanName())
                .build();
    }

    public String createRandomUrl() {
        return UUID.randomUUID().toString();
    }
}
