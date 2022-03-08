package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Image;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShowDetailResponseDto {
    private LocalDateTime planDate;

    private String planName;

    private String destination;

    private List<Image> imageList;

    private String contents;

    public ShowDetailResponseDto(Plan plan) {
        this.planDate = plan.getPlanDate();
        this.planName = plan.getPlanName();
        this.destination = plan.getDestination();
        this.imageList = plan.getImageList();
        this.contents = plan.getContents();
    }
}
