package sparta.team6.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sparta.team6.momo.model.Plan;

@Data
@AllArgsConstructor
@Builder
public class MeetResponseDto {
    private Long planId;
    private String planeName;
}
