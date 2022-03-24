package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.*;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Account;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.AccountRepository;
import sparta.team6.momo.repository.ImageRepository;
import sparta.team6.momo.repository.PlanRepository;
import sparta.team6.momo.utils.amazonS3.UploadService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static sparta.team6.momo.exception.ErrorCode.MEMBER_NOT_FOUND;
import static sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanService {


    private final PlanRepository planRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    private final AccountRepository accountRepository;
    private final MeetService meetService;

    @Transactional
    public Long savePlan(PlanRequestDto request, Long userId) {
        Plan savedPlan = planRepository.save(request.toEntity());
        Account account = accountRepository.findById(userId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        savedPlan.addPlan(account);
        savedPlan.addUrl(meetService.createRandomUrl());
        return savedPlan.getId();
    }

    @Transactional
    public void deletePlan(Long planId, Long userId) {
        Plan result = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(result.getAccount().getId())) {
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
    public PlanResponseDto updatePlan(Long planId, PlanRequestDto requestDto, Long userId) {
        Plan savedPlan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(savedPlan.getAccount().getId())) {
            savedPlan.update(requestDto);
            return PlanResponseDto.of(savedPlan);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    public DetailResponseDto showDetail(Long planId, Long userId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(plan.getAccount().getId())) {
            List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (Image image : imageList) {
                imageDtoList.add(new ImageDto(image.getId(), image.getImage()));
            }
            return DetailResponseDto.of(plan, imageDtoList);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        //TODO: 쿼리2번 조회하지말고 테이블 join해서 쿼리 한번에 가져오게 하기
        // 시도해봤으나, image가 비어있는 경우에 plan 데이터를 받아오지못해서 에러 발생함
        // plan entity가 image entity를 참조하지 않기 때문에 join 불가능(단방향 연관관계)
    }

    public List<MainResponseDto> showMain(LocalDateTime date, Long userId) {
        LocalDateTime monthStart = LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0, 0);
        LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

        List<Plan> planList = planRepository.findAllByAccountIdAndPlanDateBetween(userId, monthStart, monthEnd);
        List<MainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new MainResponseDto(plan));
        }
        return dtoList;
    }
}
