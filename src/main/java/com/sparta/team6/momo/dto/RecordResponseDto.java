package com.sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecordResponseDto {
    private Long planId;
    private LocalDateTime planDate;
    private String planName;
    private String destination;
    private boolean isFinished;

    public RecordResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.planDate = plan.getPlanDate();
        this.planName = plan.getPlanName();
        this.destination = plan.getDestination();
        this.isFinished = finishCheck(plan.getPlanDate());
    }

    public boolean finishCheck(LocalDateTime planDate) {
        return LocalDateTime.now().isAfter(planDate.plusHours(1));
    }
}
