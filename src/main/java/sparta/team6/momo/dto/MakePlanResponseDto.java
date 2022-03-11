package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakePlanResponseDto {
    private Long planId;

    public MakePlanResponseDto(Long planId) {
        this.planId = planId;
    }
}
