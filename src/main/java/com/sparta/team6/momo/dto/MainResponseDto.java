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
    private LocalDateTime noticeTime;
    private LocalDateTime finishTime;
    private String url;
    private boolean isFinished;

    public MainResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.planName = plan.getPlanName();
        this.planDate = plan.getPlanDate();
        this.noticeTime = plan.getNoticeTime();
        this.finishTime = plan.getPlanDate().plusHours(1);
        this.url = activeCheck(plan.getNoticeTime(), plan.getPlanDate().plusHours(1), plan.getUrl());
        this.isFinished = finishCheck(plan.getPlanDate());
    }

    private boolean finishCheck(LocalDateTime planDate) {
        return LocalDateTime.now().isAfter(planDate.plusHours(1));
    }

    private String activeCheck(LocalDateTime noticeTime, LocalDateTime finishTime, String url) {
        if (LocalDateTime.now().isAfter(noticeTime) && LocalDateTime.now().isBefore(finishTime)) {
            return url;
        }
        return null;
    }

}
