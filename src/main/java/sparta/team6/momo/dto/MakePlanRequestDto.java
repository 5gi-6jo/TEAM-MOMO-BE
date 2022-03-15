package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import sparta.team6.momo.model.Plan;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MakePlanRequestDto {
    @NotNull(message = "약속 날짜를 입력하세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime planDate;

    @NotBlank(message = "약속명을 입력하세요")
    private String planName;

    @NotBlank(message = "약속 장소를 입력하세요")
    private String destination;

    // Todo : 내용 + 알람시간

    // 알람시간은 분 단위 String으로 받음

    public Plan toEntity() {
        return Plan.builder()
                .planDate(planDate)
                .planName(planName)
                .destination(destination)
                .build();
    }
}
