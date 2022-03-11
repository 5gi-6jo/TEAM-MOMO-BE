package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ShowRecordResponseDto {
    private Long planId;
    private LocalDateTime planDate;
    private String planName;
    private boolean isFinished;

    //Todo: isFinished 체크

    public ShowRecordResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.planDate = plan.getPlanDate();
        this.planName = plan.getPlanName();
        this.isFinished = false;
    }
}
