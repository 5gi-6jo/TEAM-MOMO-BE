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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private static final int PAGE_SIZE = 10;

    private final PlanRepository planRepository;

    public List<RecordResponseDto> showRecord(Long pageNumber, Long accountId) {
        Pageable pageRequest = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("planDate", "createdAt").descending());
        Page<Plan> planList = planRepository.findAllByAccount_Id(accountId, pageRequest);

        if (planList.getTotalPages() <= pageNumber) {
            throw new CustomException(ErrorCode.DO_NOT_HAVE_ANY_RESOURCE);
        }
        List<RecordResponseDto> dtoList = new ArrayList<>();
        for (Plan plan : planList) {
            dtoList.add(new RecordResponseDto(plan));
        }
        return dtoList;
    }
}
