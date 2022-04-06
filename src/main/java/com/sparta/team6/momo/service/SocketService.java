package com.sparta.team6.momo.service;

import com.sparta.team6.momo.dto.MapDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlanRepository planRepository;

    public void setDestination(Long planId, MapDto mapDto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(PLAN_NOT_FOUND));
        mapDto.setDestLat(plan.getLat());
        mapDto.setDestLng(plan.getLng());
    }

    public LocalDateTime getExpireDate(Long planId) {
        Optional<Plan> plan = planRepository.findById(planId);
        return plan.map(Plan::getPlanDate).orElseThrow(() -> new CustomException(PLAN_NOT_FOUND));
    }
}
