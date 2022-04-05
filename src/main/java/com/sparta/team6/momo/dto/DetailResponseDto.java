package com.sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class DetailResponseDto {
    private String planDate;
    private String noticeTime;
    private String finishTime;
    private String planName;
    private String destination;
    private List<ImageDto> imageList;
    private String contents;
    private String url;

    @Builder
    public DetailResponseDto(String planDate, String noticeTime, String finishTime, String planName, String destination, List<ImageDto> imageList, String contents, String url) {
        this.planDate = planDate;
        this.noticeTime = noticeTime;
        this.finishTime = finishTime;
        this.planName = planName;
        this.destination = destination;
        this.contents = contents;
        this.imageList = imageList;
        this.url = url;
    }

    public static DetailResponseDto of(Plan plan, List<ImageDto> imageList) {
        return DetailResponseDto.builder()
                .planDate(plan.getPlanDate().toString())
                .noticeTime(plan.getNoticeTime().toString())
                .finishTime(plan.getPlanDate().plusHours(1).toString())
                .planName(plan.getPlanName())
                .destination(plan.getDestination())
                .contents(plan.getContents())
                .imageList(imageList)
                .url(activeCheck(plan.getNoticeTime(), plan.getPlanDate().plusHours(1), plan.getUrl()))
                .build();
    }

    private static String activeCheck(LocalDateTime noticeTime, LocalDateTime finishTime, String url) {
        if (LocalDateTime.now().isAfter(noticeTime) && LocalDateTime.now().isBefore(finishTime)) {
            return url;
        }
        return null;
    }
}
