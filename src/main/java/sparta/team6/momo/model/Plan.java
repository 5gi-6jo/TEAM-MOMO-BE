package sparta.team6.momo.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.dto.MakePlanRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Column
    private String contents;

    @Builder
    public Plan(String planName, String destination, LocalDateTime planDate) {
        this.planName = planName;
        this.destination = destination;
        this.planDate = planDate;
    }

    public Plan(MakePlanRequest request) {
        this.planDate = request.getPlanDate();
        this.planName = request.getPlanName();
        this.destination = request.getDestination();
    }
}
