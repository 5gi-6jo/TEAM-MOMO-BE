package sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

@Getter
@NoArgsConstructor
public class PlanResponseDto {
    Long planId;
    Double lat;
    Double lng;

    @Builder
    public PlanResponseDto(Long planId, Double lat, Double lng) {
        this.planId = planId;
        this.lat = lat;
        this.lng = lng;
    }

    public static PlanResponseDto of(Plan plan) {
        return PlanResponseDto.builder()
                .planId(plan.getId())
                .lat(plan.getLat())
                .lng(plan.getLng())
                .build();
    }
}
