package sparta.team6.momo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.MakePlanRequest;
import sparta.team6.momo.dto.MakePlanResponse;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

@Service
public class PlanService {

    private PlanRepository planRepository;

    @Autowired
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public MakePlanResponse savePlan(MakePlanRequest request) {
        Plan savedPlan = planRepository.save(request.toEntity());
        return new MakePlanResponse(savedPlan.getPlanId());
    }


}
