package com.sparta.team6.momo.service;

import com.sparta.team6.momo.dto.*;
import com.sparta.team6.momo.utils.amazonS3.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.model.Account;
import com.sparta.team6.momo.model.Image;
import com.sparta.team6.momo.model.Plan;
import com.sparta.team6.momo.repository.AccountRepository;
import com.sparta.team6.momo.repository.ImageRepository;
import com.sparta.team6.momo.repository.PlanRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.team6.momo.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanService {


    private final PlanRepository planRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    private final AccountRepository accountRepository;
    private final MeetService meetService;

    @Transactional
    public Long savePlan(PlanRequestDto request, Long accountId) {
        Plan savedPlan = planRepository.save(request.toEntity());
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        savedPlan.addPlan(account);
        savedPlan.addUrl(meetService.createRandomUrl());
        return savedPlan.getId();
    }

    @Transactional
    public void deletePlan(Long planId, Long accountId) {
        Plan result = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (accountId.equals(result.getAccount().getId())) {
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
    public PlanResponseDto updatePlan(Long planId, PlanRequestDto requestDto, Long accountId) {
        Plan savedPlan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (accountId.equals(savedPlan.getAccount().getId())) {
            savedPlan.update(requestDto);
            return PlanResponseDto.of(savedPlan);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    public DetailResponseDto showDetail(Long planId, Long accountId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (accountId.equals(plan.getAccount().getId())) {
            List<Image> imageList = imageRepository.findAllByPlan_Id(planId);
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (Image image : imageList) {
                imageDtoList.add(new ImageDto(image.getId(), image.getImage()));
            }
            return DetailResponseDto.of(plan, imageDtoList);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    public List<MainResponseDto> showMain(String date, Long accountId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        System.out.println(dateTime);
        LocalDateTime monthStart = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0, 0);
        LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

        List<Plan> planList = planRepository.findAllByAccountIdAndPlanDateBetween(accountId, monthStart, monthEnd);
        List<MainResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new MainResponseDto(plan));
        }
        return dtoList;
    }
}
