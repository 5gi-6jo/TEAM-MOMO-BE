package sparta.team6.momo.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.dto.MakePlanRequestDto;
import sparta.team6.momo.dto.UpdatePlanRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Column
    private String contents;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    @Builder
    public Plan(String planName, String destination, LocalDateTime planDate) {
        this.planName = planName;
        this.destination = destination;
        this.planDate = planDate;
    }

    public Plan(MakePlanRequestDto request) {
        this.planDate = request.getPlanDate();
        this.planName = request.getPlanName();
        this.destination = request.getDestination();
    }

    public void update(UpdatePlanRequestDto requestDto) {
        this.planName = requestDto.getPlanName();
        this.destination = requestDto.getDestination();
        this.planDate = requestDto.getPlanDate();
        this.contents = requestDto.getContents();
    }
}
