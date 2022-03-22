package sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShowDetailResponseDto {
    private LocalDateTime planDate;

    private String planName;

    private DestinationDto destination;

    private List<ImageDto> imageList;

    private String contents;

    @Builder
    public ShowDetailResponseDto(LocalDateTime planDate, String planName, DestinationDto destination, List<ImageDto> imageList, String contents) {
        this.planDate = planDate;
        this.planName = planName;
        this.destination = destination;
        this.imageList = imageList;
        this.contents = contents;
    }

    public static ShowDetailResponseDto of(Plan plan, List<ImageDto> imageList, DestinationDto destinationDto) {
        return ShowDetailResponseDto.builder()
                .planDate(plan.getPlanDate())
                .planName(plan.getPlanName())
                .destination(destinationDto)
                .destination(destinationDto)
                .imageList(imageList)
                .contents(plan.getContents())
                .build();
    }
}
