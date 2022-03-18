package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.team6.momo.amazonS3.UploadService;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.model.User;
import sparta.team6.momo.repository.ImageRepository;
import sparta.team6.momo.repository.PlanRepository;
import sparta.team6.momo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static sparta.team6.momo.exception.ErrorCode.MEMBER_NOT_FOUND;
import static sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanService {

    public static final int PAGE_SIZE = 5;

    private final PlanRepository planRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    private final UserRepository userRepository;


    @Transactional
    public MakePlanResponseDto savePlan(MakePlanRequestDto request, String email) {
        System.out.println("service 진입");
        Plan savedPlan = planRepository.save(request.toEntity());
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        savedPlan.addPlan(user);
        System.out.println("service 종료");
        return new MakePlanResponseDto(savedPlan.getId());
    }

    @Transactional
    public void deletePlan(Long planId, String email) {
        Plan result = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (email.equals(result.getUser().getEmail())) {
            List<Image> imageList = imageRepository.deleteAllByPlanId(planId);
            for (Image image : imageList) {
                uploadService.deleteFile(image.getImage().split(".com/")[1]);
            }
            planRepository.deleteById(planId);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    @Transactional
    public void updatePlan(Long planId, UpdatePlanRequestDto requestDto) {
        Plan savedPlan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        savedPlan.update(requestDto);
    }

    public ShowDetailResponseDto showDetail(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        List<Image> imageList = imageRepository.findAllByPlan_Id(planId);

        List<ImageDto> imageDtoList = new ArrayList<>();
        for (Image image : imageList) {
            imageDtoList.add(new ImageDto(image.getId(), image.getImage()));
        }
        //TODO: 쿼리2번 조회하지말고 테이블 join해서 쿼리 한번에 가져오게 하기
        // 시도해봤으나, image가 비어있는 경우에 plan 데이터를 받아오지못해서 에러 발생함
        // plan entity가 image entity를 참조하지 않기 때문에 join 불가능(단방향 연관관계)
        return ShowDetailResponseDto.of(plan, imageDtoList);
    }

    public List<ShowMainResponseDto> showMain(String email) {
        List<Plan> planList = planRepository.findAllByUserEmail(email);
        List<ShowMainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowMainResponseDto(plan));
        }
        return dtoList;
    }

    public List<ShowRecordResponseDto> showRecord(Long pageNumber, String email) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());
        Page<Plan> planList = planRepository.findAllByUserEmail(email, pageRequest);
        List<ShowRecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowRecordResponseDto(plan));
        }
        return dtoList;
    }
}
