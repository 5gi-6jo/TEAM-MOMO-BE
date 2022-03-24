package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.MapDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

import java.util.Optional;

import static sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;

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
}
