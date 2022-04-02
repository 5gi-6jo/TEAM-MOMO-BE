package com.sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MainResponseDto {
    private Long planId;
    private String planName;
    private LocalDateTime planDate;
    private String url;
    private boolean isFinished;

    public MainResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.planDate = plan.getPlanDate();
        this.planName = plan.getPlanName();
        this.url = plan.getUrl();
        this.isFinished = finishCheck(plan.getPlanDate());
    }

    public boolean finishCheck(LocalDateTime planDate) {
        return LocalDateTime.now().isAfter(planDate.plusHours(6));
    }
}
