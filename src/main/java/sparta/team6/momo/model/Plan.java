package sparta.team6.momo.model;

import lombok.*;
import sparta.team6.momo.dto.MakePlanRequestDto;

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

//    @Column(nullable = false)
//    private String destination;

    @Column
    private String contents;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Column(nullable = false)
    private LocalDateTime noticeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public Plan(String planName, String contents, LocalDateTime planDate, LocalDateTime noticeTime) {
        this.planName = planName;
        this.contents = contents;
        this.planDate = planDate;
        this.noticeTime = noticeTime;
    }

    public void update(MakePlanRequestDto requestDto) {
        this.planName = requestDto.getPlanName();
        this.contents = requestDto.getContents();
        this.planDate = requestDto.getPlanDate();
        this.noticeTime = requestDto.toLocalDateTIme(requestDto.getNoticeTime());
    }

    public void addPlan(Account account) {
        this.account = account;
        account.getPlanList().add(this);
    }
}
