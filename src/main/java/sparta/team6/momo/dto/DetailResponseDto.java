package sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class DetailResponseDto {
    private LocalDateTime planDate;

    private String planName;

    private String destination;

    private List<ImageDto> imageList;

    private String contents;

    @Builder
    public DetailResponseDto(LocalDateTime planDate, String planName, String destination, List<ImageDto> imageList, String contents) {
        this.planDate = planDate;
        this.planName = planName;
        this.destination = destination;
        this.contents = contents;
        this.imageList = imageList;
    }

    public static DetailResponseDto of(Plan plan, List<ImageDto> imageList) {
        return DetailResponseDto.builder()
                .planDate(plan.getPlanDate())
                .planName(plan.getPlanName())
                .destination(plan.getDestination())
                .contents(plan.getContents())
                .imageList(imageList)
                .build();
    }
}