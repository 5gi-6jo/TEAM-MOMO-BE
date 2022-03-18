package sparta.team6.momo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShowDetailResponseDto {
    private LocalDateTime planDate;

    private String planName;

    private String destination;

    private List<ImageDto> imageList;

    private String contents;

//    @Builder
//    public ShowDetailResponseDto() {
//        this.planDate = plan.getPlanDate();
//        this.planName = plan.getPlanName();
//        this.destination = plan.getDestination();
//        this.imageList = plan.getImageList();
//        this.contents = plan.getContents();
//    }

    @Builder
    public ShowDetailResponseDto(LocalDateTime planDate, String planName, String destination, List<ImageDto> imageList, String contents) {
        this.planDate = planDate;
        this.planName = planName;
        this.destination = destination;
        this.imageList = imageList;
        this.contents = contents;
    }

    public static ShowDetailResponseDto of(Plan plan, List<ImageDto> imageList) {
        return ShowDetailResponseDto.builder()
                .planDate(plan.getPlanDate())
                .planName(plan.getPlanName())
                .destination(plan.getDestination())
                .contents(plan.getContents())
                .imageList(imageList)
                .build();
    }
}
