package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ShowMainResponseDto {
    private Long planId;
    private String planName;
    private LocalDateTime planDate;
    private boolean isFinished;

    public ShowMainResponseDto(Plan plan) {
        this.planId = plan.getId();
        this.planDate = plan.getPlanDate();
        this.planName = plan.getPlanName();
        this.isFinished = finishCheck(plan.getPlanDate());
    }

    public boolean finishCheck(LocalDateTime planDate) {
        return LocalDateTime.now().isAfter(planDate.plusHours(6));
    }
}
