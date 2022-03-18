package sparta.team6.momo.model;

import lombok.*;
import sparta.team6.momo.dto.UpdatePlanRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String destination;

    @Column
    private String contents;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Column(nullable = false)
    private LocalDateTime noticeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Plan(String planName, String destination, String contents, LocalDateTime planDate, LocalDateTime noticeTime) {
        this.planName = planName;
        this.destination = destination;
        this.contents = contents;
        this.planDate = planDate;
        this.noticeTime = noticeTime;
    }

    public void update(UpdatePlanRequestDto requestDto) {
        this.planName = requestDto.getPlanName();
        this.destination = requestDto.getDestination();
        this.planDate = requestDto.getPlanDate();
        this.contents = requestDto.getContents();
    }

    public void addPlan(User user) {
        this.user = user;
        user.getPlanList().add(this);
    }
}
