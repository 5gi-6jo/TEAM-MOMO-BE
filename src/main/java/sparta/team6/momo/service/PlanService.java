package sparta.team6.momo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.ImageRepository;
import sparta.team6.momo.repository.PlanRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static sparta.team6.momo.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
public class PlanService {

    public static final int PAGE_SIZE = 5;

    private PlanRepository planRepository;
    private ImageRepository imageRepository;

    @Autowired
    public PlanService(PlanRepository planRepository, ImageRepository imageRepository) {
        this.planRepository = planRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public MakePlanResponseDto savePlan(MakePlanRequestDto request) {
        Plan savedPlan = planRepository.save(request.toEntity());
        return new MakePlanResponseDto(savedPlan.getId());
    }

    @Transactional
    public void deletePlan(Long planId) {
        planRepository.deleteById(planId);
    }

    @Transactional
    public void updatePlan(Long planId, UpdatePlanRequestDto requestDto) {
        Plan savedPlan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        savedPlan.update(requestDto);
    }

    public ShowDetailResponseDto showDetail(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
        List<ImageDto> imageDto = new ArrayList<>();
        for (Image image : imageList) {
            imageDto.add(new ImageDto(image.getImage()));
        }

        return ShowDetailResponseDto.builder()
                .contents(plan.getContents())
                .planDate(plan.getPlanDate())
                .planName(plan.getPlanName())
                .destination(plan.getDestination())
                .imageList(imageDto)
                .build();
    }

    public List<ShowMainResponseDto> showMain() {
        List<Plan> planList = planRepository.findAll();
        List<ShowMainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowMainResponseDto(plan));
        }
        return dtoList;
    }

    public List<ShowRecordResponseDto> showRecord(Long pageNumber) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());
        Page<Plan> planList = planRepository.findAll(pageRequest);
//        System.out.println(planRepository.count());
        List<ShowRecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowRecordResponseDto(plan));
        }
        return dtoList;
    }

}
