package sparta.team6.momo.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import sparta.team6.momo.model.Plans;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MakePlanRequest {
    @NotNull(message = "약속 날짜를 입력하세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime planDate;

    @NotBlank(message = "약속명을 입력하세요")
    private String planName;

    @NotBlank(message = "약속 장소를 입력하세요")
    private String destination;

    public Plans toEntity() {
        return Plans.builder()
                .planDate(planDate)
                .planName(planName)
                .destination(destination)
                .build();


    }
}
