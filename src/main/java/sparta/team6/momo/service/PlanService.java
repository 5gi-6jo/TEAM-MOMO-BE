package sparta.team6.momo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanService {

    public static final int PAGE_SIZE = 5;

    private PlanRepository planRepository;

    @Autowired
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional
    public MakePlanResponseDto savePlan(MakePlanRequestDto request) {
        Plan savedPlan = planRepository.save(request.toEntity());
        return new MakePlanResponseDto(savedPlan.getId());
    }

    @Transactional
    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    @Transactional
    public void updatePlan(Long id, UpdatePlanRequestDto requestDto) {
        Plan savedPlan = planRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id가 존재하지 않습니다")
        );
        savedPlan.update(requestDto);
    }

    public ShowDetailResponseDto showDetail(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 id가 존재하지 않습니다")
        );
        return new ShowDetailResponseDto(plan);
    }

    public List<ShowMainResponseDto> showMain() {
        List<Plan> planList = planRepository.findAll();
        List<ShowMainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            ShowMainResponseDto responseDto = new ShowMainResponseDto(plan);
            dtoList.add(responseDto);
        }
        return dtoList;
    }

    public List<ShowRecordResponseDto> showRecord(int pageNumber) {
        Pageable pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("planDate", "createdAt").descending());
        Page<Plan> planList = planRepository.findAll(pageRequest);
//        System.out.println(planRepository.count());
        List<ShowRecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            ShowRecordResponseDto responseDto = new ShowRecordResponseDto(plan);
            dtoList.add(responseDto);
        }
        return dtoList;
    }

}
