package com.sparta.team6.momo.service;

import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sparta.team6.momo.dto.RecordResponseDto;
import com.sparta.team6.momo.model.Plan;
import com.sparta.team6.momo.repository.PlanRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    public static final int PAGE_SIZE = 5;

    private final PlanRepository planRepository;

    public List<RecordResponseDto> showRecord(Long pageNumber, Long period, Long accountId) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tempDate = currentTime.minusDays(period - 1);
        LocalDateTime startDate = LocalDateTime.of(tempDate.getYear(), tempDate.getMonth(), tempDate.getDayOfMonth(), 0, 0, 0);
        Page<Plan> planList = planRepository.findAllByUserIdAndPlanDateBetween(accountId, startDate, currentTime, pageRequest);

        if (planList.getTotalPages() <= pageNumber) {
            throw new CustomException(ErrorCode.DO_NOT_HAVE_ANY_RESOURCE);
        }

        List<RecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new RecordResponseDto(plan));
        }
        return dtoList;
    }

    public List<RecordResponseDto> searchRecord(String keyword, Long pageNumber, Long accountId) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());

        Page<Plan> searchResult = planRepository.findAllByUserIdAndPlanNameContaining(accountId, keyword, pageRequest);
        if (searchResult.getTotalPages() <= pageNumber) {
            throw new CustomException(ErrorCode.DO_NOT_HAVE_ANY_RESOURCE);
        }

        List<RecordResponseDto> dtoList = new ArrayList<>();
        for (Plan result : searchResult) {
            dtoList.add(new RecordResponseDto(result));
        }
        return dtoList;
    }
}
