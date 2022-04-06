package com.sparta.team6.momo.service;

import com.sparta.team6.momo.dto.MeetResponseDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.model.Plan;
import com.sparta.team6.momo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.sparta.team6.momo.exception.ErrorCode.INVALID_MAP_URL;
import static com.sparta.team6.momo.exception.ErrorCode.MEET_URI_GONE;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final PlanRepository planRepository;

    public MeetResponseDto getPlanInfo(String url) {

        Plan plan = planRepository.findPlanByUrl(url).
                orElseThrow(() -> new CustomException(INVALID_MAP_URL));

        if (isPlanEnd(plan)) throw new CustomException(MEET_URI_GONE);


        return new MeetResponseDto(plan.getId(), plan.getPlanName());
    }

    private boolean isPlanEnd(Plan plan) {
        return plan.getPlanDate().plusHours(1L).isBefore(LocalDateTime.now());
    }
}
