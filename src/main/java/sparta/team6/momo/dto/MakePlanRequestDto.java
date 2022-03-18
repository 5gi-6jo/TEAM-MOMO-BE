package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import sparta.team6.momo.model.Plan;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MakePlanRequestDto {

    @Length(max = 20, message = "20자 이내로 입력해주세요")
    @NotBlank(message = "모임 이름을 입력해주세요")
    private String planName;

    @NotBlank(message = "장소를 입력해주세요")
    private String destination;

    @Length(max = 50, message = "50자 이내로 입력해주세요")
    private String contents;

    @NotNull(message = "모임 시간을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime planDate;

    // 알람시간은 분 단위 String으로 받음
    @Positive
    @NotNull(message = "모두모여 시간을 설정해주세요")
    private Long noticeTime;

    private LocalDateTime toLocalDateTIme(Long noticeTime) {
        return planDate.minusMinutes(noticeTime);
    }

    public Plan toEntity() {
        return Plan.builder()
                .planName(planName)
                .destination(destination)
                .contents(contents)
                .planDate(planDate)
                .noticeTime(toLocalDateTIme(noticeTime))
                .build();
    }
}
