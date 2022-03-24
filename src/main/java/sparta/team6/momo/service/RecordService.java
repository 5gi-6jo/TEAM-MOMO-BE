package sparta.team6.momo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.ShowRecordResponseDto;
import sparta.team6.momo.exception.CustomException;
import sparta.team6.momo.exception.ErrorCode;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    public static final int PAGE_SIZE = 5;

    private static PlanRepository planRepository;


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
