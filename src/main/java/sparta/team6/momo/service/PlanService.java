package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public static final int PAGE_SIZE = 5;

    private final PlanRepository planRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    private final AccountRepository accountRepository;
    private final MeetService meetService;

    @Transactional
    public Long savePlan(MakePlanRequestDto request, Long userId) {
        Plan savedPlan = planRepository.save(request.toEntityPlan());
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
    public void updatePlan(Long planId, MakePlanRequestDto requestDto, Long userId) {
        Plan savedPlan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(savedPlan.getAccount().getId())) {
            savedPlan.update(requestDto);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    public ShowDetailResponseDto showDetail(Long planId, Long userId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(plan.getAccount().getId())) {
            List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (Image image : imageList) {
                imageDtoList.add(new ImageDto(image.getId(), image.getImage()));
            }
            return ShowDetailResponseDto.of(plan, imageDtoList);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        //TODO: 쿼리2번 조회하지말고 테이블 join해서 쿼리 한번에 가져오게 하기
        // 시도해봤으나, image가 비어있는 경우에 plan 데이터를 받아오지못해서 에러 발생함
        // plan entity가 image entity를 참조하지 않기 때문에 join 불가능(단방향 연관관계)
    }

    public List<ShowMainResponseDto> showMain(LocalDateTime date, Long userId) {
        LocalDateTime monthStart = LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0, 0);
        LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

        List<Plan> planList = planRepository.findAllByAccountIdAndPlanDateBetween(userId, monthStart, monthEnd);
        List<ShowMainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowMainResponseDto(plan));
        }
        return dtoList;
    }

    public List<ShowRecordResponseDto> showRecord(Long pageNumber, Long period, Long userId) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startDate = currentTime.minusDays(period - 1);
        startDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0);

        Page<Plan> planList = planRepository.findAllByAccountIdAndPlanDateBetween(userId, startDate, currentTime, pageRequest);

        if (planList.getTotalPages() <= pageNumber) {
            throw new CustomException(ErrorCode.DO_NOT_HAVE_ANY_RESOURCE);
        }

        List<ShowRecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new ShowRecordResponseDto(plan));
        }
        return dtoList;
    }

    public List<ShowRecordResponseDto> searchRecord(String word, Long pageNumber, Long userId) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());

        Page<Plan> searchResult = planRepository.findAllByAccountIdAndPlanNameContaining(userId, word, pageRequest);
        if (searchResult.getTotalPages() <= pageNumber) {
            throw new CustomException(ErrorCode.DO_NOT_HAVE_ANY_RESOURCE);
        }

        List<ShowRecordResponseDto> dtoList = new ArrayList<>();
        for (Plan result : searchResult) {
            dtoList.add(new ShowRecordResponseDto(result));
        }
        return dtoList;
    }
}
